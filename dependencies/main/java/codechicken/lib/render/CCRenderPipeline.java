package codechicken.lib.render;

import codechicken.lib.render.CCRenderState.IVertexOperation;
import codechicken.lib.render.CCRenderState.VertexAttribute;

import java.util.ArrayList;

public class CCRenderPipeline
{
    public class PipelineBuilder
    {
        public PipelineBuilder add(IVertexOperation op) {
            ops.add(op);
            return this;
        }

        public PipelineBuilder add(IVertexOperation... ops) {
            for(int i = 0; i < ops.length; i++)
                CCRenderPipeline.this.ops.add(ops[i]);
            return this;
        }

        public void build() {
            rebuild();
        }

        public void render() {
            rebuild();
            CCRenderState.render();
        }
    }

    private class PipelineNode
    {
        public ArrayList<PipelineNode> deps = new ArrayList<PipelineNode>();
        public IVertexOperation op;

        public void add() {
            if(op == null)
                return;

            for(int i = 0; i < deps.size(); i++)
                deps.get(i).add();
            deps.clear();
            sorted.add(op);
            op = null;
        }
    }

    private ArrayList<VertexAttribute> attribs = new ArrayList<VertexAttribute>();
    private ArrayList<IVertexOperation> ops = new ArrayList<IVertexOperation>();
    private ArrayList<PipelineNode> nodes = new ArrayList<PipelineNode>();
    private ArrayList<IVertexOperation> sorted = new ArrayList<IVertexOperation>();
    private PipelineNode loading;
    private PipelineBuilder builder = new PipelineBuilder();

    public void setPipeline(IVertexOperation... ops) {
        this.ops.clear();
        for(int i = 0; i < ops.length; i++)
            this.ops.add(ops[i]);
        rebuild();
    }

    public void reset() {
        ops.clear();
        unbuild();
    }

    private void unbuild() {
        for(int i = 0; i < attribs.size(); i++)
            attribs.get(i).active = false;
        attribs.clear();
        sorted.clear();
    }

    public void rebuild() {
        if(ops.isEmpty() || CCRenderState.model == null)
            return;

        //ensure enough nodes for all ops
        while(nodes.size() < CCRenderState.operationCount())
            nodes.add(new PipelineNode());
        unbuild();

        if(CCRenderState.useNormals)
            addAttribute(CCRenderState.normalAttrib);
        if(CCRenderState.useColour)
            addAttribute(CCRenderState.colourAttrib);
        if(CCRenderState.computeLighting)
            addAttribute(CCRenderState.lightingAttrib);

        for(int i = 0; i < ops.size(); i++) {
            IVertexOperation op = ops.get(i);
            loading = nodes.get(op.operationID());
            boolean loaded = op.load();
            if(loaded)
                loading.op = op;

            if(op instanceof VertexAttribute)
                if(loaded)
                    attribs.add((VertexAttribute)op);
                else
                    ((VertexAttribute)op).active = false;
        }

        for(int i = 0; i < nodes.size(); i++)
            nodes.get(i).add();
    }

    public void addRequirement(int opRef) {
        loading.deps.add(nodes.get(opRef));
    }

    public void addDependency(VertexAttribute attrib) {
        loading.deps.add(nodes.get(attrib.operationID()));
        addAttribute(attrib);
    }

    public void addAttribute(VertexAttribute attrib) {
        if(!attrib.active) {
            ops.add(attrib);
            attrib.active = true;
        }
    }

    public void operate() {
        for(int i = 0; i < sorted.size(); i++)
            sorted.get(i).operate();
    }

    public PipelineBuilder builder() {
        ops.clear();
        return builder;
    }
}
