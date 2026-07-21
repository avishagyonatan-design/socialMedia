/** מחלקה המייצגת פןסט ברשת חברתית
 * המחלקה אחראית על שמירת תוכן הפוסט, המחבר שלו, זמן פרסןם ומספר לייקים
 * מכילה את תוכן הפוסט, מונה הלייקים ורשימת התגובות שניכתבו עליו
 */



import java.time.LocalDateTime;
import java.util.ArrayList;


public class Post {


    private static int counter = 1;


    private ArrayList<Comment> comments;
    private ArrayList<User> likeBy;


    private int id;
    private User author;
    private String content;
    private LocalDateTime timestamp;
    private int likesCount;

    /**
     * בנאי היוצר פוסט חדש
     * @param author אובייקט המשתמש שכתב את הפוסט
     * @param content תוכן הפוסט
     */
    public Post(User author, String content) throws EmptyContentException{
        if (content == null || content.trim().isEmpty()){
            throw new EmptyContentException();
        }
        this.comments = new ArrayList<>();
        this.likeBy = new ArrayList<>();
        this.id = counter++;//קידום המונה ID
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();//אתחול תאריך הפרסןם
        this.likesCount = 0;//פוסט חדש מתחיל תמיד עם 0 לייקים

    }

    /**
     * מתודה המחזירה מספר מזהה של הפוסט
     * @return המספר המזהה
     */
    public int getId(){
        return id;
    }

    /**
     * מתודה המחזירה אובייקט המשתמש שכתב את הפוסט
     * @return מחבר הפוסט
     */
    public User getAuthor(){
        return author;
    }
    
    public String getContent(){
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getLikesCount() {
        return likesCount;
    }
    public void addLike(User user) throws AlreadyLikedException{
        if (this.likeBy.contains(user)){
            throw new AlreadyLikedException();
        }
        this.likeBy.add(user);//קודם כל מוסיפים את המשתמש שלחץ על הלייק לתוך הרשימה
        this.likesCount = this.likeBy.size();//מעדכנים את מספר הלייקים שיהיה שווה בדיוק לכמות האנשים שיש עכשיו ברשימה
    }
    public void removeLike(){
        this.likesCount--;//מורידה לייק
    }
    public String toString(){
        int day = timestamp.getDayOfMonth();
        int month = timestamp.getMonthValue();
        int year = timestamp.getYear();
        int hour = timestamp.getHour();
        int minute = timestamp.getMinute();

        //טיפול בדקות ובשעות שיוצגו יפה
        String minuteStr = " ";
        if (minute < 10 ){
            minuteStr = "0" + minute;
        }else {
            minuteStr = " " + minute;
        }
        String hourStr = " ";
        if (hour < 10 ){
            hourStr = "0" + hour;
        }else {
            hourStr = " " + hour;
        }
        //נבנה תאריך ושעה
        String timestemp = day + "." + month + "." + year + " " + hourStr + " : " + minuteStr;

        // מחזירים את המחרוזת עם פרטי פוסט לפי הפורמט
        return "post #" + id + " | @" + author.getUserName() + " | " + content + " | Likes: " + likesCount + " | " + timestemp;
    }

    /**
     * מוסיפה תגובה חדשה לרשימת התגובות של הפוסט ומעדכנת את המזהה שלה
     * @param comment התגובה שנוספה לפוסט
     */
    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setId(this.comments.size());
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getCommentCount(){
        return comments.size();
    }

    /**
     * מדפיסה את כל התגובות שניכתבו על הפוסט
     */
    public void printComments(){
        if (this.comments.isEmpty()){
            System.out.println("No comments");
        }else {
            for (Comment comment : this.comments){
                System.out.println(comment);
            }
        }
    }


}
