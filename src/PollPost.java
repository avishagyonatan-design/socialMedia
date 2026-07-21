import java.util.ArrayList;

public class PollPost extends Post {

    private ArrayList<String> options;
    /**
     * בנאי היוצר מופע חדש של פוסט מסוג סקר עם אפשרויות בחירה.
     * הפעולה בודקת שהסקר מכיל מספר מינימלי של אפשרויות בחירה תקינות.
     * @param author  המשתמש (User) שפרסם את הסקר.
     * @param content השאלה או התוכן המרכזי של הסקר.
     * @param options רשימת אפשרויות הבחירה הזמינות להצבעה בסקר.
     * @throws EmptyContentException   אם השאלה/תוכן הסקר ריקים.
     * @throws InvalidOptionException  אם רשימת האפשרויות מכילה פחות מ-2 אפשרויות בחירה.
     */
    public PollPost(User author, String content,ArrayList<String> options) throws EmptyContentException, InvalidOptionException{
        super(author, content);
        if (options == null || options.size() < 2){
            throw new InvalidOptionException();
        }
        this.options = options;
    }
    /**
     * מחזירה את רשימת אפשרויות הבחירה שהוגדרו עבור הסקר.
     * @return ArrayList של מחרוזות המייצגות את האפשרויות בסקר.
     */
    public ArrayList<String> getOptions(){
        return options;
    }

    @Override
    public String toString() {
        return "post #" + this.getId() + " | @" +this.getAuthor().getUserName() + " | " + this.getContent() +" | " + options + " | " +  " | Likes: " + this.getLikesCount() + " | " + this.getTimestamp();
    }
}
