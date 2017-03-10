package codechicken.lib.render.shader.pipeline;

import codechicken.lib.render.shader.ShaderProgram;
import codechicken.lib.render.shader.pipeline.attribute.IShaderOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by covers1624 on 18/10/2016.
 */
public class CCShaderPipeline {

    private static int nextOperationIndex;

    public static int registerOperation() {
        return nextOperationIndex++;
    }

    public static int operationCount() {
        return nextOperationIndex;
    }

    public class PipelineBuilder {

        public CCShaderPipeline.PipelineBuilder add(IShaderOperation op) {
            ops.add(op);
            return this;
        }

        public CCShaderPipeline.PipelineBuilder add(IShaderOperation... ops) {
            for (int i = 0; i < ops.length; i++) {
                CCShaderPipeline.this.ops.add(ops[i]);
            }
            return this;
        }

        public void build() {
            rebuild();
        }
    }

    private class PipelineNode {

        public ArrayList<CCShaderPipeline.PipelineNode> deps = new ArrayList<CCShaderPipeline.PipelineNode>();
        public IShaderOperation op;

        public void add() {
            if (op == null) {
                return;
            }

            for (int i = 0; i < deps.size(); i++) {
                deps.get(i).add();
            }
            deps.clear();
            sorted.add(op);
            op = null;
        }
    }

    private ShaderProgram program;

    private ArrayList<IShaderOperation> ops = new ArrayList<IShaderOperation>();
    private ArrayList<CCShaderPipeline.PipelineNode> nodes = new ArrayList<CCShaderPipeline.PipelineNode>();
    private ArrayList<IShaderOperation> sorted = new ArrayList<IShaderOperation>();
    private CCShaderPipeline.PipelineNode loading;
    private CCShaderPipeline.PipelineBuilder builder = new CCShaderPipeline.PipelineBuilder();

    public CCShaderPipeline(ShaderProgram program) {
        this.program = program;
    }

    public void setPipeline(IShaderOperation... ops) {
        this.ops.clear();
        Collections.addAll(this.ops, ops);
        rebuild();
    }

    public void setPipeline(List<IShaderOperation> ops) {
        this.ops.clear();
        this.ops.addAll(ops);
        rebuild();
    }

    public void reset() {
        ops.clear();
        unbuild();
    }

    private void unbuild() {
        sorted.clear();
    }

    public void rebuild() {

        if (ops.isEmpty()) {
            return;
        }

        //ensure enough nodes for all ops
        while (nodes.size() < operationCount()) {
            nodes.add(new CCShaderPipeline.PipelineNode());
        }
        unbuild();

        for (IShaderOperation op : ops) {
            loading = nodes.get(op.operationID());
            boolean loaded = op.load(program);
            if (loaded) {
                loading.op = op;
            }
        }

        for (PipelineNode node : nodes) {
            node.add();
        }
    }

    public void addRequirement(int opRef) {
        loading.deps.add(nodes.get(opRef));
    }

    public void operate() {
        for (IShaderOperation op : sorted) {
            op.operate(program);
        }
    }

    public CCShaderPipeline.PipelineBuilder builder() {
        ops.clear();
        return builder;
    }
}
