# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes SourceFile, LineNumberTable, Exceptions, InnerClasses, Signature
-keepattributes RuntimeVisibleAnnotations, AnnotationDefault
-keepattributes JavascriptInterface

-keepclassmembers class * extends android.webkit.WebViewClient {
    <methods>;
}

-dontwarn com.googlecode.**
-dontwarn com.squareup.okhttp.**

#javacpp/javacv
-keep class com.googlecode.** {
	*;
}

-keep class com.moxtra.** {
	*;
}

#webrtc
-keep class org.webrtc.** {
	*;
}

-keep class com.unitt.** {
	*;
}

# keep this class so that logging will show 'ACRA' and not a obfuscated name like 'a'.
# Note: if you are removing log messages elsewhere in this file then this isn't necessary
-keep class org.acra.** {
	*;
}

#otto
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# Keep SafeParcelable value, needed for reflection. This is required to support backwards
# compatibility of some classes.
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

# Keep the names of classes/members we need for client functionality.
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keep class android.support.**{
*;
}

-keep class uk.co.chrisjenx.**{
    *;
}

# Needed for Parcelable/SafeParcelable Creators to not get stripped
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class org.jsoup.**{
    *;
}
