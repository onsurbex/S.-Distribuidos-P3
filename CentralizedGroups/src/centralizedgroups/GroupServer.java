/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedgroups;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author onsur
 */
public class GroupServer extends UnicastRemoteObject implements GroupServerInterface{

    LinkedList<ObjectGroup> groupList;
    int id = 0;
    ReentrantLock lock;
    
    GroupServer() throws RemoteException {
        
    }
    
    
    @Override
    public int createGroup(String groupAlias, String ownerAlias, String hostname) {
        this.lock.lock();
        try {
            if(findGroup(groupAlias) == -1){
                ObjectGroup group = new ObjectGroup(groupAlias,id,ownerAlias,hostname);
                groupList.add(group);
                id++;
                return group.groupID;
            } else {
                return -1;
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int findGroup(String groupAlias) {
        this.lock.lock();
        try {
            for (ObjectGroup group : groupList) {
                if(group.groupAlias.equals(groupAlias))
                    return group.groupID;
            }
            return -1;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public String findGroup(int groupID) {
        this.lock.lock();
        try {
            for (ObjectGroup group : groupList) {
                if(group.groupID == groupID)
                    return group.groupAlias;
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean removeGroup(String groupAlias, String ownerAlias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GroupMember addMember(String groupAlias, String alias, String hostname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeMember(String groupAlias, String alias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GroupMember isMember(String groupAlias, String alias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean StopMembers(String groupAlias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean AllowMembers(String groupAlias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<String> ListMembers(String groupAlias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<String> ListGroup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
