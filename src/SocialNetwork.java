import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *@author  Avishag yonatan - 215110081
 * מחלקה המייצגת את מערכת הרשת החברתית אחראית על ניהול ריכוזי של כל המשתמשים, פוסטים והאינטראקציות במערכת
 */
public class SocialNetwork {


    private String name;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();


    public SocialNetwork(String name){
        this.name = name;
    }

    /**
     * מחפשת משתמש ברשת החברתית על פי שם המשתמש הייחודי שלו.
     *
     * @param username שם המשתמש אותו מחפשים במערכת.
     * @return אובייקט ה-User שנמצא במערכת.
     * @throws UserNotFoundException אם לא נמצא אף משתמש עם שם המשתמש המבוקש.
     */
    public User findUserByUsername(String username) throws UserNotFoundException{
        for (User us : this.users){
            if (us.getUserName().equals(username)){
                return us;
            }
        }
        throw new UserNotFoundException();
    }

    /**
     * מוסיפה משתמש חדש למערכת, בתנאי ששם המשתמש שלו אינו קיים כבר
     * @param user אובייקט המשתמש להוספה לרשת
     * @return אם שם המשתמש כבר קיים במערכת FALSE, אם שם המשתמש הוסף הבצלחה אז TRUE
     */
    public boolean addUser(User user){
        try {
            findUserByUsername(user.getUserName());
            return false;
        } catch (UserNotFoundException e) {
            this.users.add(user);
            return true;
        }
    }

    /**
     * יוצרת פוסט טקסט רגיל חדש ברשת, ושומרת אותו במערכת ובאובייקט המשתמש.
     * @param author  המשתמש המפרסם את הפוסט.
     * @param content תוכן הטקסט של הפוסט.
     * @return אובייקט ה-Post החדש שנוצר והתווסף למערכת.
     * @throws EmptyContentException אם המשתמש ניסה לפרסם פוסט ללא תוכן.
     */
    public Post createPost(User author,String content) throws EmptyContentException{

            Post post = new Post(author,content);
            this.posts.add(post);
            author.addPost(post);
            return post;
    }
    /**
     * מוסיפה לייק לפוסט מסוים ממשתמש, ומייצרת התראה עבור מחבר הפוסט (אם זה לא לייק עצמי).
     * @param post הפוסט שמקבל את הלייק.
     * @param user המשתמש שלחץ על כפתור הלייק.
     * @throws AlreadyLikedException אם המשתמש כבר ביצע לייק לפוסט זה בעבר.
     */
    public void likePost(Post post,User user) throws AlreadyLikedException{
        post.addLike(user);
        if (user != post.getAuthor()){
            Notification notification = new Notification(user, post.getAuthor(), user.getUserName() + "liked your post");
            post.getAuthor().addNotification(notification);
        }
    }

    /**
     * מוסיפה תגובה לפוסט שכבר קיים ושולחת התראה אוטומטית לבעל הפוסט
     * @param author המשתמש שכתב את התגובה
     * @param post הפוסט שעליו מגיבים
     * @param content תוכן התגובה
     * @return אובייקט התגובה החדש שנוצר
     */
    public Comment addComment(User author, Post post, String content){
        Comment comment = new Comment(author, post, content);
        post.addComment(comment);
        Notification notification = new Notification(author, post.getAuthor(), author.getUserName() + " commented on your post");
        post.getAuthor().addNotification(notification);
        return comment;
    }

    /**
     * מחפשת פוסטים שהתוכן שלהם מכיל מחרוזת טקסט מסוימת
     * @param text מילת החיפןש
     * @return רשימה  של כל הפוסטיםהמכילים את הטקסט המבוקש.
     */
    public ArrayList<Post> searchPosts(String text){
        ArrayList<Post> results = new ArrayList<>();//יצירת רשימת התוצאות
        for (Post p : this.posts){//מעבר על כל הפוסטים ברשת
            if (p.getContent().contains(text)){//האם התוכן של הפוסט הספיציפי מכיל את מילת החיפוש?
                results.add(p);//מצאנו התאמה , נוסיף את הפוסט לסל התוצאות
            }
        }
        return results;//מחזירים את סל התוצאות
    }

    /**
     * מדפיסה את כל המשתמשים הרשומים ברשת החברתית
     */
    public void printAllUsers() {
        if (this.users.isEmpty()){
            System.out.println("No users to display");
        }else {
            for (User user : this.users){
                System.out.println(user);
            }
        }
    }

    /**
     * מדפיסה את כל הפוסטים שפורסמו ברשת החברתית
     */
    public void printAllPosts(){
        if (this.posts.isEmpty()){
            System.out.println("No posts to display");
        }else {
            for (Post post : this.posts){
                System.out.println(post);
            }
        }
    }
    /**
     * יוצרת פוסט אירוע חדש ברשת החברתית, ומוסיפה אותו למאגר הפוסטים הכללי ולרשימת המשתמש.
     * @param author        המשתמש שמפרסם ומארגן את האירוע.
     * @param content       תיאור או פרטי האירוע.
     * @param eventDateTime תאריך ושעת קיום האירוע.
     * @param location      מיקום האירוע.
     * @return אובייקט ה-EventPost החדש שנוצר.
     * @throws EmptyContentException אם תוכן תיאור האירוע ריק.
     */
    public EventPost createEventPost(User author, String content, LocalDateTime eventDateTime, String location) throws EmptyContentException{
        EventPost eventPost = new EventPost(author, content, eventDateTime, location);
        this.posts.add(eventPost);
        author.addPost(eventPost);
        return eventPost;
    }
    /**
     * יוצרת פוסט סקר חדש ברשת החברתית, ומוסיפה אותו למאגר הפוסטים הכללי ולרשימת המשתמש.
     *
     * @param author  המשתמש שמפרסם את הסקר.
     * @param content השאלה או כותרת הסקר.
     * @param options רשימת האפשרויות לבחירה.
     * @return אובייקט ה-PollPost החדש שנוצר.
     * @throws EmptyContentException  אם כותרת הסקר ריקה.
     * @throws InvalidOptionException אם הועברו פחות מ-2 אפשרויות בחירה להצבעה.
     */
    public PollPost createPollPost(User author, String content,ArrayList<String> options) throws EmptyContentException,InvalidOptionException{
        PollPost pollPost = new PollPost(author, content, options);
        this.posts.add(pollPost);
        author.addPost(pollPost);
        return pollPost;
    }
    public ArrayList<User> getUsers(){
        return this.users;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
