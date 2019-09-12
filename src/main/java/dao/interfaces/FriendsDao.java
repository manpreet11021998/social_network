package dao.interfaces;

import model.User;

import java.util.Collection;

public interface FriendsDao extends Dao {

    void addFriend(User user, User friend);
    void removeFriend(User user, User friend);
    long getNumberOfFriends(User user, String searchText);
    Collection<User> getFriendList(User user, int recordsPerPage, int i, String searchText);

}
