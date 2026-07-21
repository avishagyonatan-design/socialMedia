public class EmptyContentException extends ApplicationException{

    public EmptyContentException(){
        super("Post content cannot be empty");
    }
}
