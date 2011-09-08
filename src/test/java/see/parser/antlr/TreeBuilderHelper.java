package see.parser.antlr;

import org.antlr.runtime.ClassicToken;
import see.parser.antlr.tree.*;
import see.parser.config.GrammarConfiguration;

/**
 * @author pavlov
 * @since 08.09.11
 */
public class TreeBuilderHelper {

    public static TransitionNode<Object> tr(SeeTreeNode<Object> arg){
        TransitionNode<Object> ret = new TransitionNode<Object>(new ClassicToken(-1, "return"));
        ret.addChild(arg);
        return ret;
    }

    public static OperatorNode<Object, Object> seq(GrammarConfiguration gc, SeeTreeNode<Object>... elements){
        OperatorNode<Object, Object> seq = new OperatorNode<Object, Object>(
                new ClassicToken(-1, null),
                gc.getFunctions().get("seq")
        );
        for (SeeTreeNode<Object> elem : elements){
            seq.addChild(elem);
        }
        return seq;
    }

    public static SimpleFunctionNode<Object, Object> fun(GrammarConfiguration gc, String functionName, SeeTreeNode<Object>... args){
        SimpleFunctionNode<Object, Object> func
                = new SimpleFunctionNode<Object, Object>(new ClassicToken(-1, functionName), gc.getFunctions().get(functionName));
        func.addChild(new DummyNode(new ClassicToken(-1, functionName)));
        func.addChild(new DummyNode(new ClassicToken(-1, "(")));
        func.addChild(new DummyNode(new ClassicToken(-1, ")")));
        for (SeeTreeNode<Object> arg : args){
            func.addChild(arg);
        }
        return func;
    }

    public static OperatorNode<Object,Object> op(GrammarConfiguration gc, String opName, SeeTreeNode<Object>... args){
        OperatorNode<Object,Object> op = new OperatorNode<Object, Object>(new ClassicToken(-1, opName), gc.getFunctions().get(opName));
        for (SeeTreeNode<Object> arg: args){
            op.addChild(arg);
        }
        return op;
    }

    public static ConstantTreeNode<Object> str(String value){
        return new ConstantTreeNode<Object>(new ClassicToken(-1,value), value);
    }

    public static ConstantTreeNode<Object> num(GrammarConfiguration gc, String value){
        return new ConstantTreeNode<Object>(new ClassicToken(-1, value), gc.getNumberFactory().getNumber(value));
    }

    public static VarTreeNode<Object> var(String value){
        ClassicToken token = new ClassicToken(-1, value);
        return new VarTreeNode<Object>(token);
    }

}
