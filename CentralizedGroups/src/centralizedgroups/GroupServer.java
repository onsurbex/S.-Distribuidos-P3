/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedgroups;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author onsur
 */
public class GroupServer extends UnicastRemoteObject implements GroupServerInterface{

    LinkedList<ObjectGroup> groupList;
    int id = 0;
    ReentrantLock lock;
    
    GroupServer() throws RemoteException {
        this.lock = new ReentrantLock();
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
        this.lock.lock();
        try {
            for(int i = 0; i<groupList.size(); i++){
                if(groupList.get(i).groupAlias.equals(groupAlias)){
                    groupList.remove(i);
                    this.lock.unlock();
                    return true;
                }
            }
        } finally {
            this.lock.unlock();
        }
        return false;
    }

    @Override
    public GroupMember addMember(String groupAlias, String alias, String hostname) {
        this.lock.lock();
        try {
            for(ObjectGroup ob : groupList){
                if(ob.groupAlias.equals(groupAlias)){
                    for(GroupMember gm : ob.memberList){
                        if(gm.alias.equals(alias)){
                            this.lock.unlock();
                            return null;
                        }
                    }
                    try {
                        ob.addMember(alias, hostname);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GroupServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for(GroupMember gm : ob.memberList){
                        if(gm.alias.equals(alias)){
                            this.lock.unlock();
                            return gm;
                        }
                    }
                }
            }
        } finally {
            this.lock.unlock();
        }
        return null;
    }

    @Override
    public boolean removeMember(String groupAlias, String alias) {
        this.lock.lock();
        try {
            for(ObjectGroup ob : groupList){
                if(ob.groupAlias.equals(groupAlias)){
                    for(int i = 0; i<ob.memberList.size(); i++){
                        if(ob.memberList.get(i).alias.equals(alias)){
                            if(ob.owner.alias.equals(alias)){
                                //cannot remove owner
                                this.lock.unlock();
                                return false;
                            }
                            else {
                                //success
                                ob.memberList.remove(i);
                                this.lock.unlock();
                                return true;
                            }
                        }
                    }
                    //not found in group
                    this.lock.unlock();
                    return false;
                }
            }
            //group not found
        } finally {
            this.lock.unlock();
        }
        return false;
    }

    @Override
    public GroupMember isMember(String groupAlias, String alias) {
        this.lock.lock();
        try {
            for(ObjectGroup ob: groupList){
                if(ob.groupAlias.equals(groupAlias)){
                    //found group
                    for(GroupMember gm : ob.memberList){
                        if(gm.alias.equals(alias)){
                            this.lock.unlock();
                            return gm;
                        }
                    }
                    //member not found in group
                    this.lock.unlock();
                    return null;
                }
            }
        //group not found
        } finally {
            this.lock.unlock();
        }
        return null;
    }

    @Override
    public boolean StopMembers(String groupAlias) {
        for(ObjectGroup ob: groupList){
            if(ob.groupAlias.equals(groupAlias)){
                ob.StopMembers();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean AllowMembers(String groupAlias) {
        for(ObjectGroup ob: groupList){
            if(ob.groupAlias.equals(groupAlias)){
                ob.AllowMembers();
                return true;
            }
        }
        return false;
    }

    @Override
    public LinkedList<String> ListMembers(String groupAlias) {
        this.lock.lock();
        try {
            for(ObjectGroup ob: groupList){
                if(ob.groupAlias.equals(groupAlias)){
                    LinkedList<String> namelist = new LinkedList<>();
                    for(GroupMember gm: ob.memberList){
                        namelist.add(gm.alias);
                    }
                    return namelist;
                }
            }
        } finally {
            this.lock.unlock();
        }
        return null;
    }

    @Override
    public LinkedList<String> ListGroup() {
        this.lock.lock();
        LinkedList<String> namelist = new LinkedList<>();
        try {
            for(ObjectGroup ob: groupList){
                namelist.add(ob.groupAlias);
            }
        } finally {
            this.lock.unlock();
        }
        return namelist;
    }
    
    public static void main(String[] args){
        System.setProperty("java.security.policy", "C:\\Users\\onsur\\Documents\\NetBeansProjects\\S.-Distribuidos-P3\\CentralizedGroups");
        System.setSecurityManager(new SecurityManager());
        try {
            LocateRegistry.createRegistry(1099);  
            GroupServer groupServer = new GroupServer();
            Naming.rebind("GroupServer", groupServer);
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(GroupServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
