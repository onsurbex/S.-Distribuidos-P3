/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedgroups;

import java.rmi.Remote;
import java.util.LinkedList;

/**
 *
 * @author onsur
 */
public interface GroupServerInterface extends Remote{
    
    /**
     * 
     * @param groupAlias Group alias identifier
     * @param ownerAlias Group owner identifier
     * @param ownerHostname Hostname of the owner
     * @return  n>=0 - Group identifier integer greater or equal than 0
     *            -1 - Error  
     */
    public int createGroup(String groupAlias,String ownerAlias, String ownerHostname);
    
    public int findGroup(String groupAlias);
    
    public String findGroup(int groupID);
    
    public boolean removeGroup(String groupAlias, String ownerAlias);
    
    public GroupMember addMember(String groupAlias, String alias, String hostname);
    
    public boolean removeMember(String groupAlias, String alias);
    
    public GroupMember isMember(String groupAlias, String alias);
    
    public boolean StopMembers(String groupAlias);
    
    public boolean AllowMembers(String groupAlias);
    
    public LinkedList<String> ListMembers(String groupAlias);
    
    public LinkedList<String> ListGroup();
}
