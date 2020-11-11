public class Debugger {

    private Class debugee;
    private int[] breakpointLines;

    public Class getDebugee() {
        return debugee;
    }

    public void setDebugee(Class debugee) {
        this.debugee = debugee;
    }

    public int[] getBreakpointLines() {
        return breakpointLines;
    }

    public void setBreakpointLines(int[] breakpointLines) {
        this.breakpointLines = breakpointLines;
    }
}
