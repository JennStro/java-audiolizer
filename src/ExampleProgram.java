public class ExampleProgram {

    public String helloWorld() {
        Guitar guitar = new Guitar();
        guitar.hello();
        return "Hello world!";
    }

    public static void main(String[] args) {

        ExampleProgram exampleProgram = new ExampleProgram();
        exampleProgram.helloWorld();

        Guitar guitar = new Guitar();
        guitar.hello();

    }

}
