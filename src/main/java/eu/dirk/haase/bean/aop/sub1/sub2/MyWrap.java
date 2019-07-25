package eu.dirk.haase.bean.aop.sub1.sub2;

public class MyWrap {

    private final MyWrap wrap;

    public MyWrap() {
        this.wrap = null;
    }

    public MyWrap(final MyWrap wrap) {
        this.wrap = wrap;
    }


    public void myCall() {
        if (this.wrap != null) {
            this.wrap.myCall();
        } else {
            System.out.println("Hallo");
        }
    }
}
