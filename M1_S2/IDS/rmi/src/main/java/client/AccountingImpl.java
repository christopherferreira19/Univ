package client;

import shared.Accounting;

import java.rmi.RemoteException;

public class AccountingImpl implements Accounting {

    private static int count = 0;
    private final int id = ++count;

    @Override
    public void numberOfCall(int number) throws RemoteException {
        System.out.println("Accounting:" + id + " have:" + number);
    }
}
