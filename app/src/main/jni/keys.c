#include <jni.h>

JNIEXPORT jstring
Java_com_itesm_esenciapatrimonio_ui_LoginFragment_getUsername(JNIEnv *env, jobject instance) {
 return (*env)-> NewStringUTF(env, "MiPatrimonio");
}

JNIEXPORT jstring
Java_com_itesm_esenciapatrimonio_ui_LoginFragment_getPassword(JNIEnv *env, jobject instance) {
 return (*env)-> NewStringUTF(env, "R35t4Ur4D0!");
}