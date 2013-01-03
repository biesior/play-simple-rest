import models.User;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    public void onStart(play.Application application){

        if (User.find.findRowCount()==0){

            User u1 = new User("Alice", "Ali Baba");
            u1.save();

            User u2 = new User("Bob", "bobby_x");
            u2.save();

            User u3 = new User("Charles", "charlie");
            u3.save();

        }

    }

}
