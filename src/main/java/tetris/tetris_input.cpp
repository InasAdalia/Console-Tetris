#include <jni.h>
#include <iostream>
#include <windows.h>
#include "tetris_TetrisInput.h" // The generated JNI header file

JNIEXPORT void JNICALL Java_tetris_TetrisInput_getKeyPress(JNIEnv *env, jobject obj){
    // jclass inputClass = env->FindClass("tetris/TetrisInput");
    jclass inputClass = env->GetObjectClass(obj);

    if (!inputClass) {
        std::cout << "[C++] ERROR: Could not find TetrisInput class!" << std::endl;
        return;
    }

    jmethodID keyDown = env->GetMethodID(inputClass, "keyDown", "()V");
    jmethodID keyRight = env->GetMethodID(inputClass, "keyRight", "()V");
    jmethodID keyLeft = env->GetMethodID(inputClass, "keyLeft", "()V");
    jmethodID keyRotateR = env->GetMethodID(inputClass, "keyRotateR", "()V"); //X
    jmethodID keyRotateL = env->GetMethodID(inputClass, "keyRotateL", "()V"); //Z
    jmethodID keySpace = env->GetMethodID(inputClass, "keySpace", "()V");
    jmethodID keyEsc = env->GetMethodID(inputClass, "keyEsc", "()V");
    jmethodID keyPause = env->GetMethodID(inputClass, "keyPause", "()V"); //P
    jmethodID keyRestart = env->GetMethodID(inputClass, "keyRestart", "()V"); //R
    // jmethodID resetKeyPressed = env->GetMethodID(inputClass, "resetKeyPressed", "()V");

    if (!keyDown || !keyRight || !keyLeft || !keyEsc|| !keySpace) {
        std::cout << "[C++] ERROR: Could not find keys method!" << std::endl;
        return;
    }

    // Register hotkeys
    RegisterHotKey(NULL, 0, 0x00, VK_ESCAPE);
    RegisterHotKey(NULL, 1, 0x00, VK_DOWN);
    RegisterHotKey(NULL, 2, 0x00, VK_RIGHT);
    RegisterHotKey(NULL, 3, 0x00, VK_LEFT);
    RegisterHotKey(NULL, 4, 0x00, 0x58); // X key (0x58 in hex)
    RegisterHotKey(NULL, 5, 0x00, 0x5A); // Z key (0x5A in hex)
    RegisterHotKey(NULL, 6, 0x00, VK_SPACE);
    RegisterHotKey(NULL, 7, 0x00, 0x50); // P key (0x50 in hex)
    RegisterHotKey(NULL, 8, 0x00, 0x52); // R key (0x52 in hex)

    MSG msg;
    std::cout << "[C++] Listening for key presses..." << std::endl;
    while (true) {
        if (GetMessage(&msg, NULL, 0, 0)) {
            if (msg.message == WM_HOTKEY) {
                int id = (int)msg.wParam;

                switch (id) {
                    case 0:
                        env->CallVoidMethod(obj, keyEsc);
                        
                        break;
                    case 1:
                        env->CallVoidMethod(obj, keyDown);
                        
                        break;
                    case 2:
                        env->CallVoidMethod(obj, keyRight);
                        break;
                    case 3:
                        env->CallVoidMethod(obj, keyLeft);
                        break;
                    case 4:
                        env->CallVoidMethod(obj, keyRotateR);
                        break;
                    case 5:
                        env->CallVoidMethod(obj, keyRotateL);
                        break;
                    case 6:
                        env->CallVoidMethod(obj, keySpace);
                        break;
                    case 7:
                        env->CallVoidMethod(obj, keyPause);
                        break;
                    case 8:
                        env->CallVoidMethod(obj, keyRestart);
                        break;
                    default:
                        break;
                }

                // if (env->ExceptionCheck()) {
                //     env->ExceptionDescribe();  // Print Java exceptions if any
                //     env->ExceptionClear();     // Clear the exception so it doesn't break JNI calls
                // }
            }
        }
    }
}
