import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.*;
/**
 * Created by jo930_000 on 2016-11-07.
 */
public class MiniCPrintListener extends MiniCBaseListener {

    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();
    public static final String INDENTDOT = "....";
    int depth = 0;

    boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
        return ctx.getChildCount() ==3 && ctx.getChild(1) != ctx.expr() && ctx.getChild(0) != ctx.IDENT();
    }

    @Override
    public void exitProgram(MiniCParser.ProgramContext ctx) {
        // decl+
        for(int i = 0; i < ctx.decl().size(); i++) {
            System.out.println(newTexts.get(ctx.decl(i)));
        }
    }

    @Override
    public void exitDecl(MiniCParser.DeclContext ctx) {
        // var_decl
        if (ctx.getChild(0) == ctx.var_decl()) newTexts.put(ctx, newTexts.get(ctx.var_decl()));
            // fun_decl
        else if(ctx.getChild(0) == ctx.fun_decl()) newTexts.put(ctx, newTexts.get(ctx.fun_decl()));
    }

    @Override
    public void exitVar_decl(MiniCParser.Var_declContext ctx) {
        String s1, s2, s3, s4, s5, s6;
        s1 = newTexts.get(ctx.type_spec());
        s2 = ctx.IDENT().getText();
        s3 = ctx.getChild(2).getText();

        if(ctx.getChildCount() == 3) {
            // type_spec IDENT ';'
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3);
        }
        else if(ctx.getChildCount() == 5) {
            // type_spec IDENT '=' LITERAL ';'
            s4 = ctx.LITERAL().getText();
            s5 = ctx.getChild(4).getText();
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + " " + s3 + " " + s4 + s5);
        }
        else if(ctx.getChildCount() == 6) {
            // type_spec IDENT '[' LITERAL ']' ';'
            s4 = ctx.LITERAL().getText();
            s5 = ctx.getChild(4).getText();
            s6 = ctx.getChild(5).getText();
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + s4 + s5 + s6);
        }
    }

    @Override
    public void exitType_spec(MiniCParser.Type_specContext ctx) {
        newTexts.put(ctx, ctx.getText());
    }

    @Override
    public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
        this.depth++;
    }

    @Override
    public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
        // type_spec IDENT '(' params ')' compound_stmt
        this.depth--;
        String s1, s2, s3, s4 ,s5 ,s6;
        s1 = newTexts.get(ctx.type_spec());
        s2 = ctx.IDENT().getText();
        s3 = ctx.getChild(2).getText();
        s4 = newTexts.get(ctx.params());
        s5 = ctx.getChild(4).getText();
        s6 = newTexts.get(ctx.compound_stmt());
        newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + s4 + s5 + "\n" + this.getIndentDot() + "{\n" + s6 + this.getIndentDot() + "}\n");
    }

    @Override
    public void exitParams(MiniCParser.ParamsContext ctx) {
        if (ctx.getChildCount() == 0) {
            // nothing
            newTexts.put(ctx, "");
        }
        else if (ctx.getChild(0) == ctx.VOID()) {
            // VOID
            newTexts.put(ctx, "");
        }
        else { // param (',' param)*
            String parameter_str = newTexts.get(ctx.param(0));
            for(int i = 1; i < ctx.param().size(); i++) {
                parameter_str = parameter_str +", " + newTexts.get(ctx.param(i));
            }
            newTexts.put(ctx, parameter_str);
        }
    }

    @Override
    public void exitParam(MiniCParser.ParamContext ctx) {
        String s1, s2, s3, s4;
        // type_spec IDENT
        s1 = newTexts.get(ctx.type_spec());
        s2 = ctx.IDENT().getText();
        if(ctx.getChildCount() == 2) {
            newTexts.put(ctx, s1 + " " + s2);
        }
        else { // type_spec IDENT '[' ']'
            s3 = ctx.getChild(2).getText();
            s4 = ctx.getChild(3).getText();
            newTexts.put(ctx, s1 + " " + s2 + s3 + s4);
        }
    }

    @Override
    public void exitStmt(MiniCParser.StmtContext ctx) {
        newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
        //if(ctx.getChild(0) == ctx.expr_stmt()) {
          //  newTexts.put(ctx, newTexts.get(ctx.expr_stmt()));
        //}
        //else if(ctx.getChild(0) == ctx.compound_stmt()) {
        //    newTexts.put(ctx, newTexts.get(ctx.compound_stmt()));
        //}
        //else if(ctx.getChild(0) == ctx.if_stmt()) {
        //newTexts.put(ctx, newTexts.get(ctx.if_stmt()));
        //}
        //else if(ctx.getChild(0) == ctx.while_stmt()) {
        //    newTexts.put(ctx, newTexts.get(ctx.while_stmt()));
        //}
        //else if(ctx.getChild(0) == ctx.return_stmt()) {
        //    newTexts.put(ctx, newTexts.get(ctx.return_stmt()));
        //}
    }

    @Override
    public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        // expr ';'
        newTexts.put(ctx, this.getIndentDot() + newTexts.get(ctx.expr()) + ctx.getChild(1).getText() + "\n");
    }

    @Override
    public void enterWhile_stmt(MiniCParser.While_stmtContext ctx) {
        this.depth++;
    }

    @Override
    public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
        // WHILE '(' expr ')' stmt
        this.depth--;
        String s1, s2, s3, s4, s5;
        s1 = ctx.WHILE().getText();
        s2 = ctx.getChild(1).getText();
        s3 = newTexts.get(ctx.expr());
        s4 = ctx.getChild(3).getText();
        s5 = newTexts.get(ctx.stmt());
        newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + s4 + "\n" + this.getIndentDot() + "{\n" + s5 + this.getIndentDot() + "}\n");
    }

    @Override
    public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        // '{' local_decl* stmt* '}'
        String s1, s2;
        s1 = "";
        s2 = "";
        for(int i = 0; i < ctx.local_decl().size(); i++) {
            s1 = s1 + newTexts.get(ctx.local_decl(i));
        }
        if(ctx.local_decl().size() != 0) {
            s1 = s1 + "\n";
        }
        for(int i = 0; i < ctx.stmt().size(); i++) {
            s2 = s2 + newTexts.get(ctx.stmt(i));
        }
        newTexts.put(ctx, s1 + s2);
    }

    @Override
    public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
        String s1,s2,s3,s4,s5,s6;
        s1 = newTexts.get(ctx.type_spec());
        s2 = ctx.IDENT().getText();
        s3 = ctx.getChild(2).getText();

        if (ctx.getChildCount() == 3) {
            // type_spec IDENT ';'
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + "\n");
        }
        else if(ctx.getChildCount() == 5) {
            // type_spec IDENT '=' LITERAL ';'
            s4 = ctx.LITERAL().getText();
            s5 = ctx.getChild(4).getText();
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 +" "+ s3 + " " + s4 + s5 + "\n");
        }
        else if(ctx.getChildCount() == 6) {
            // type_spec IDENT '[' LITERAL ']' ';'
            s4 = ctx.LITERAL().getText();
            s5 = ctx.getChild(4).getText();
            s6 = ctx.getChild(5).getText();
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + s4 + s5 + s6 + "\n");
        }
    }

    @Override
    public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {
        this.depth++;
    }

    @Override
    public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
        // IF '(' expr ')' stmt
        this.depth--;
        String s1,s2,s3,s4,s5,s6,s7;
        s1 = ctx.IF().getText();
        s2 = ctx.getChild(1).getText();
        s3 = newTexts.get(ctx.expr());
        s4 = ctx.getChild(3).getText();
        s5 = newTexts.get(ctx.stmt(0));
        if(ctx.getChildCount() == 5) {
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + s4 + "\n" + this.getIndentDot() +
                    "{\n"+ s5 + this.getIndentDot() + "}\n");
        }
        else {
            // IF '(' expr ')' stmt ELSE stmt
            s6 = ctx.ELSE().getText();
            s7 = newTexts.get(ctx.stmt(1));
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + s4
                    + "\n" + this.getIndentDot() + "{\n" + s5 + this.getIndentDot() + "}\n"
                    + this.getIndentDot() + s6 + "\n" + this.getIndentDot() + "{\n" + s7
                    + this.getIndentDot() +"}\n");
        }
    }

    @Override
    public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        String s1, s2, s3;
        if (ctx.getChildCount() == 2) {
            // RETURN ';'
            s1 = ctx.getChild(0).getText();
            s2 = ctx.getChild(1).getText();
            newTexts.put(ctx, this.getIndentDot() + s1 + s2 + "\n");
        }
        else {
            // RETURN expr ';'
            s1 = ctx.getChild(0).getText();
            s2 = newTexts.get(ctx.expr());
            s3 = ctx.getChild(2).getText();
            newTexts.put(ctx, this.getIndentDot() + s1 + " " + s2 + s3 + "\n");
        }
    }

    @Override
    public void exitExpr(MiniCParser.ExprContext ctx) {
        String s1, s2, s3, s4, s5, s6;
        if (isBinaryOperation(ctx)) {
            s1 = newTexts.get(ctx.expr(0));
            s2 = ctx.getChild(1).getText();
            s3 = newTexts.get(ctx.expr(1));
            newTexts.put(ctx, s1 + " " + s2 + " " + s3);
        }
        else if (ctx.getChildCount() == 2 && ctx.getChild(0).getText().length() == 1) {
            s1 = ctx.getChild(0).getText();
            s2 = newTexts.get(ctx.expr(0));
            newTexts.put(ctx, s1 + " " + s2);
        }
        else if (ctx.getChildCount() == 2 && ctx.getChild(0).getText().length() == 2) {
            s1 = ctx.getChild(0).getText();
            s2 = newTexts.get(ctx.expr(0));
            newTexts.put(ctx, s1 + s2);
        }
        else if (ctx.getChildCount() == 1) {
            // LITERAL or IDENT
            s1 = ctx.getChild(0).getText();
            newTexts.put(ctx, s1);
        }
        else if (ctx.getChildCount() == 3 && ctx.getChild(1) == ctx.expr()) {
            // '(' expr ')'
            s1 = ctx.getChild(0).getText();
            s2 = newTexts.get(ctx.expr(0));
            s3 = ctx.getChild(2).getText();
            newTexts.put(ctx, s1 + s2 + s3);
        }
        else if (ctx.getChildCount() == 4) {
            s1 = ctx.IDENT().getText();
            s2 = ctx.getChild(1).getText();
            if(ctx.getChild(2) == ctx.expr(0)) {
                // IDENT '[' expr ']'
                s3 = newTexts.get(ctx.expr(0));
            }
            else {
                // IDENT '(' args ')'
                s3 = newTexts.get(ctx.args());
            }
            s4 = ctx.getChild(3).getText();
            newTexts.put(ctx, s1 + s2 + s3 + s4);
        }
        else if(ctx.getChildCount() == 6) {
            // IDENT '[' expr ']' '=' expr
            s1 = ctx.IDENT().getText();
            s2 = ctx.getChild(1).getText();
            s3 = newTexts.get(ctx.expr(0));
            s4 = ctx.getChild(3).getText();
            s5 = ctx.getChild(4).getText();
            s6 = newTexts.get(ctx.expr(1));
            newTexts.put(ctx, s1 + s2 + s3 + s4 + " " + s5 + " " + " " + s6);
        }
        else {
            // IDENT '=' expr
            s1 = ctx.IDENT().getText();
            s2 = ctx.getChild(1).getText();
            s3 = newTexts.get(ctx.expr(0));
            newTexts.put(ctx, s1 + " " + s2 + " " + s3);
        }
    }

    @Override
    public void exitArgs(MiniCParser.ArgsContext ctx) {  //expr (',' expr)*
        if (ctx.getChildCount() != 0 ) {
            String arg_String = newTexts.get(ctx.expr(0));

            for(int i = 1; i < ctx.expr().size(); i++) {
                arg_String = arg_String + ", " + newTexts.get(ctx.expr(i));
            }
            newTexts.put(ctx, arg_String);
        }
        else {
            newTexts.put(ctx, "");
        }
    }

    public String getIndentDot() {
        String indent = "";
        for(int i = 0; i < this.depth; i++) indent = indent + INDENTDOT;
        return indent;
    }
}