// IWebAidlInterface.aidl
package space.zhupeng.arch.web;

// Declare any non-default types here with import statements

interface IWebAidlInterface {
    void handleWebAction(String actionName);

    void startActivity(String action);
}
