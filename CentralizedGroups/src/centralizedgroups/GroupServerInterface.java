/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedgroups;

import java.util.LinkedList;

/**
 *
 * @author onsur
 */
public interface GroupServerInterface {
    
    /**
     * 
     * @param groupAlias Group alias identifier
     * @param ownerAlias Group owner identifier
     * @param ownerHostname Hostname of the owner
     * @return  n>=0 - Group identifier integer greater or equal than 0
     *            -1 - Error  
     */
    int createGroup(String groupAlias,String ownerAlias, String ownerHostname);
    
    int findGroup(String groupAlias);
    
    String findGroup(int groupID);
    
    boolean removeGroup(String groupAlias, String ownerAlias);
    
    GroupMember addMember(String groupAlias, String alias, String hostname);
    
    boolean removeMember(String groupAlias, String alias);
    
    GroupMember isMember(String groupAlias, String alias);
    
    boolean StopMembers(String groupAlias);
    
    boolean AllowMembers(String groupAlias);
    
    LinkedList<String> ListMembers(String groupAlias);
    
    LinkedList<String> ListGroup();
}
