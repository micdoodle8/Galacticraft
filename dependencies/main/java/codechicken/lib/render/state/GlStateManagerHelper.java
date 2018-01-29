package codechicken.lib.render.state;

import com.google.common.base.Objects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by covers1624 on 10/10/2016.
 * Dumps specific GL states from GlStateManager
 * Ability to push and pop most of the GL state.
 * TODO Ability to auto compare between different saved states. So basically a GlLeak analyser.
 * TODO Support both Raw GL and GLStateManager, would be useful for detecting GLLeaks that occur due to raw GL calls.
 * TODO Support all the things.
 */
public class GlStateManagerHelper {

    public static enum State {
        GL_ALPHA_TEST {
            @Override
            public String getState() {
                AlphaState alphaState = GlStateManager.alphaState;
                return Objects.toStringHelper(name()).add("Func", alphaState.func).add("Ref", alphaState.ref).add("Enabled", String.valueOf(alphaState.alphaTest.currentState).toUpperCase()).toString();
            }

            @Override
            public void pushState(SavedState state) {
                state.alphaTest = GlStateManager.alphaState.alphaTest.currentState;
                state.alphaFunc = GlStateManager.alphaState.func;
                state.alphaRef = GlStateManager.alphaState.ref;
            }

            @Override
            public void popState(SavedState pre, SavedState post) {
                if (pre.alphaTest) {
                    GlStateManager.enableAlpha();
                } else {
                    GlStateManager.disableAlpha();
                }
                GlStateManager.alphaFunc(pre.alphaFunc, pre.alphaRef);
            }
        },
        GL_LIGHTING {
            @Override
            public String getState() {
                return Objects.toStringHelper(name()).add("Enabled", parseBoolState(GlStateManager.lightingState)).toString();
            }

            @Override
            public void pushState(SavedState state) {
                state.lighting = GlStateManager.lightingState.currentState;
            }

            @Override
            public void popState(SavedState pre, SavedState post) {
                if (pre.lighting) {
                    GlStateManager.enableLighting();
                } else {
                    GlStateManager.disableLighting();
                }
            }
        },
        GL_BLEND {
            @Override
            public String getState() {
                BlendState blendState = GlStateManager.blendState;
                //@formatter:off
                return Objects.toStringHelper(name())
                        .add("SrcFactor", parseFactor(blendState.srcFactor))          .add("DstFactor", parseFactor(blendState.dstFactor))
                        .add("SrcFactorAlpha", parseFactor(blendState.srcFactorAlpha)).add("DstFactorAlpha", parseFactor(blendState.dstFactorAlpha))
                        .add("Enabled", parseBoolState(blendState.blend)).toString();
                //@formatter:on
            }

            @Override
            public void pushState(SavedState state) {
                state.blend = GlStateManager.blendState.blend.currentState;
                state.blendSrcFactor = GlStateManager.blendState.srcFactor;
                state.blendDstFactor = GlStateManager.blendState.dstFactor;
                state.blendSrcFactorAlpha = GlStateManager.blendState.srcFactorAlpha;
                state.blendDstFactorAlpha = GlStateManager.blendState.dstFactorAlpha;
            }

            @Override
            public void popState(SavedState pre, SavedState post) {
                if (pre.blend) {
                    GlStateManager.enableBlend();
                } else {
                    GlStateManager.disableBlend();
                }
                if (pre.blendSrcFactorAlpha != post.blendSrcFactorAlpha || pre.blendDstFactorAlpha != post.blendDstFactorAlpha) {
                    GlStateManager.tryBlendFuncSeparate(pre.blendSrcFactor, pre.blendDstFactor, pre.blendSrcFactorAlpha, pre.blendDstFactorAlpha);
                } else {
                    GlStateManager.blendFunc(pre.blendSrcFactor, post.blendDstFactor);
                }
            }

            private String parseFactor(int factor) {
                switch (factor) {
                    case 32771:
                        return "CONSTANT_ALPHA";
                    case 32769:
                        return "CONSTANT_COLOR";
                    case 772:
                        return "DST_ALPHA";
                    case 774:
                        return "DST_COLOR";
                    case 1:
                        return "ONE";
                    case 32772:
                        return "ONE_MINUS_CONSTANT_ALPHA";
                    case 32770:
                        return "ONE_MINUS_CONSTANT_COLOR";
                    case 773:
                        return "ONE_MINUS_DST_ALPHA";
                    case 775:
                        return "ONE_MINUS_DST_COLOR";
                    case 771:
                        return "ONE_MINUS_SRC_ALPHA";
                    case 769:
                        return "ONE_MINUS_SRC_COLOR";
                    case 770:
                        return "SRC_ALPHA";
                    case 776:
                        return "SRC_ALPHA_SATURATE";
                    case 768:
                        return "SRC_COLOR";
                    case 0:
                        return "ZERO";
                    default:
                        return "UNKNOWN:" + factor;

                }
            }

        },
        GL_DEPTH_TEST {
            @Override
            public String getState() {
                DepthState depthState = GlStateManager.depthState;
                return Objects.toStringHelper(name()).add("Func", parseFunc(depthState.depthFunc)).add("Mask", String.valueOf(depthState.maskEnabled).toUpperCase()).add("Enabled", parseBoolState(depthState.depthTest)).toString();
            }

            @Override
            public void pushState(SavedState state) {
                state.depthTest = GlStateManager.depthState.depthTest.currentState;
                state.depthMaskEnabled = GlStateManager.depthState.maskEnabled;
                state.depthFunc = GlStateManager.depthState.depthFunc;
            }

            @Override
            public void popState(SavedState pre, SavedState post) {
                if (pre.depthTest) {
                    GlStateManager.enableDepth();
                } else {
                    GlStateManager.disableDepth();
                }
                GlStateManager.depthMask(pre.depthMaskEnabled);
                GlStateManager.depthFunc(pre.depthFunc);
            }

            private String parseFunc(int func) {
                switch (func) {
                    case 0x200:
                        return "GL_NEVER";
                    case 0x201:
                        return "GL_LESS";
                    case 0x202:
                        return "GL_EQUAL";
                    case 0x203:
                        return "GL_LEQUAL";
                    case 0x204:
                        return "GL_GREATER";
                    case 0x205:
                        return "GL_NOTEQUAL";
                    case 0x206:
                        return "GL_GEQUAL";
                    case 0x207:
                        return "GL_ALWAYS";
                    default:
                        return "UNKNOWN:" + func;

                }
            }
        },
        GL_CULL_FACE {
            @Override
            public String getState() {
                CullState cullState = GlStateManager.cullState;
                return Objects.toStringHelper(name()).add("Mode", parseMode(cullState.mode)).add("Enabled", parseBoolState(cullState.cullFace)).toString();
            }

            @Override
            public void pushState(SavedState state) {
                state.cull = GlStateManager.cullState.cullFace.currentState;
                state.cullMode = GlStateManager.cullState.mode;
            }

            @Override
            public void popState(SavedState pre, SavedState post) {

                if (pre.cull) {
                    GlStateManager.enableCull();
                } else {
                    GlStateManager.disableCull();
                }
                GlStateManager.cullFace(pre.cullMode);
            }

            private String parseMode(int mode) {
                switch (mode) {
                    case 1028:
                        return "FRONT";
                    case 1029:
                        return "BACK";
                    case 1032:
                        return "FRONT_AND_BACK";
                    default:
                        return "UNKNOWN:" + mode;

                }
            }

        },
        GL_RESCALE_NORMAL {
            @Override
            public String getState() {
                return Objects.toStringHelper(name()).add("Enabled", parseBoolState(GlStateManager.rescaleNormalState)).toString();
            }

            @Override
            public void pushState(SavedState state) {
                state.rescaleNormal = GlStateManager.rescaleNormalState.currentState;
            }

            @Override
            public void popState(SavedState pre, SavedState post) {
                if (pre.rescaleNormal) {
                    GlStateManager.enableRescaleNormal();
                } else {
                    GlStateManager.disableRescaleNormal();
                }
            }

        };

        public abstract String getState();

        public abstract void pushState(SavedState state);

        public abstract void popState(SavedState pre, SavedState post);
    }

    public static class SavedState {

        private ArrayList<State> savedStates = new ArrayList<State>();

        boolean alphaTest;
        int alphaFunc;
        float alphaRef;

        boolean lighting;

        boolean blend;
        int blendSrcFactor;
        int blendDstFactor;
        int blendSrcFactorAlpha;
        int blendDstFactorAlpha;

        boolean depthTest;
        boolean depthMaskEnabled;
        int depthFunc;

        boolean cull;
        int cullMode;

        boolean rescaleNormal;

        private SavedState() {
        }

        private static SavedState createSavedState(SavedState state) {
            return createSavedState(state.savedStates.toArray(new State[state.savedStates.size()]));
        }

        private static SavedState createSavedState(State... states) {
            SavedState saveState = new SavedState();
            for (State state : states) {
                state.pushState(saveState);
                saveState.savedStates.add(state);
            }
            return saveState;
        }

        private static void applyState(SavedState pre, SavedState post) {
            for (State state : pre.savedStates) {
                state.popState(pre, post);
            }
        }

    }

    private static final LinkedList<SavedState> savedStates = new LinkedList<SavedState>();

    private static String parseBoolState(BooleanState boolState) {
        return String.valueOf(boolState.currentState).toUpperCase();
    }

    public static String dumpGLState(State... states) {
        StringBuilder builder = new StringBuilder();
        builder.append("GlStateManager { ");
        for (int i = 0; i < states.length; i++) {
            State state = states[i];
            builder.append(state.getState());
            if (i != states.length - 1) {
                builder.append(", ");
            }
        }

        builder.append(" }");
        return builder.toString();
    }

    public static void pushState() {
        SavedState state = SavedState.createSavedState(State.values());
        savedStates.addLast(state);
    }

    public static void pushStates(State... states) {
        if (states == null || states.length == 0) {
            states = State.values();
        }
        SavedState state = SavedState.createSavedState(states);
        savedStates.addLast(state);
    }

    public static void pushState(State[] states) {
        if (states == null || states.length == 0) {
            states = State.values();
        }
        SavedState state = SavedState.createSavedState(states);
        savedStates.addLast(state);
    }

    public static void popState() {
        if (savedStates.isEmpty()) {
            throw new IllegalStateException("Unable to pop the GL state as there is no saved state!");
        }
        SavedState pre = savedStates.removeLast();
        SavedState post = SavedState.createSavedState(pre);
        SavedState.applyState(pre, post);
    }

}
