// IMyAidlInterface.aidl
package com.example.learnbinder;

// Declare any non-default types here with import statements

interface IMyAidlInterface {

     String test(String data);

     void transBinder(IBinder binder);

     String testBinder();

}