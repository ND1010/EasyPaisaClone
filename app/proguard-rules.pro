# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Viral\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#Lottie Animation
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** {*;}

#ICICI MICRO ATM
-keepattributes Signature
-keepattributes Annotation
-dontwarn sun.misc.**
-keep class com.fingpay.microatmsdk.data.** {
    <fields>;
    public <methods>;
}
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}


#Searchview
-keep class androidx.appcompat.widget.SearchView { *; }
-keep class android.support.v7.widget.SearchView { *; }

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializerSplashActivity

## Fresco Rules
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-dontwarn org.apache.**
-keep class com.android.volley.** {*;}
#-keep class in.org.npci.commonlibrary.** {*;}
#-keep class org.npci.upi.security.** {*;}
-keep class javax.naming.** {*;}
#-keep class javax.naming.directory.** {*;}
-keep class javax.naming.directory.** {*;}
-keep class aepsapp.easypay.com.aepsandroid.entities.** {*;}

#-keep class com.atom.mobilepaymentsdk.** {*;}
-keep class net.bohush.geometricprogressview.** {*;}

-dontwarn org.bouncycastle.**

#-keepclassmembers class com.atom.mobilepaymentsdk.MyJavaScriptInterface {
#      public *;
#}

-keepclassmembers class * {
      @android.webkit.JavascriptInterface <methods>;
}


-keepattributes Exceptions, Signature, InnerClasses
-keepattributes JavascriptInterface

#-keep class com.atom.ipg.DATA.security.** { *; }

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


# For using GSON @Expose annotation
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-dontwarn com.crashlytics.**

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
-dontwarn com.sun.org.apache.html.dom.**
-dontwarn javax.servlet.**
-dontwarn org.joda.time.**
-dontwarn org.w3c.dom.**
-dontwarn com.atom.ipg.DATA.security.**
-dontwarn android.support.**
-dontwarn org.apache.**
-dontwarn javax.xml.parsers.**
-dontwarn org.kxml2.**
-dontwarn javax.xml.parsers.DocumentBuilder.parse.**


-keepattributes Signature
# For using GSON @Expose annotation

-keepattributes *Annotation*
-keep public class com.google.android.gms.* { public *; }
-keepclassmembers class fqcn.of.javascript.interface.for.webview { *; }
-keepclassmembers class * { @android.webkit.JavascriptInterface <methods>; }


-keep class javax.xml.parsers.DocumentBuilder.parse.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider { public <init>(android.content.Context); }
-keep interface android.support.v4.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.** { *; } #################################################################### REMOVE WARNINGS
-dontwarn android.support.design.internal.**
-dontwarn android.support.v4.** # Enable proguard with Google libs
-keep class com.google.** { *;}
-dontwarn com.google.common.**
#-dontwarn com.google.ads.** ###################################################################### #support-v7
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
 -dontwarn org.**
 -keep class org.xmlpull.v1.** { *;}
  -dontwarn org.xmlpull.v1.**

-dontwarn javax.xml.** ################################################################## ##https://github.com/excilys/androidannotations/issues/1341
-dontwarn org.androidannotations.api.rest.**
-keep class org.apache.** { *; }
#-keep class rjsv.floatingmenu.** { *; }
#-keepclassmembers class rjsv.floatingmenu.** { <fields>; }
-keep class java.net.** { *; } # http://stackoverflow.com/questions/29679177/cardview-shadow-not-appearing-in-lollipop-after-obfuscate-with-proguard/29698051
-keep class android.support.v7.widget.RoundRectDrawable { *; } ###################################################################### #support-v7
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider { public <init>(android.content.Context); } ####################################################################### #support-design -dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
################################################# #CrashAnalytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

################################################# #Gson
-keep class com.android.volley.** { *; }
-keep class org.apache.commons.logging.**


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory, # JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.stream.** { *; }

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# Application classes that will be serialized/deserialized over Gson #
#-keep class com.mgs.sbiupi.common.DATA.models.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**
-dontwarn org.bouncycastle.asn1.DERInputStream
-dontwarn org.apache.xml.utils.**
-dontwarn  org.apache.xpath.**

-keep class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator *; }
-keep class org.apache.http.** { *; }
-keep public class org.simpleframework.**{ *; }
-keep class org.simpleframework.xml.**{ *; }
-keep class org.simpleframework.xml.core.**{ *; }
-keep class org.simpleframework.xml.util.**{ *; }
-keep class com.shashank.sony.fancytoastlibrary.**{ *; }

-keep class com.google.android.gms.** { *; }
#-keep class com.google.android.gms.ads.** { *; }
#-dontwarn com.google.android.gms.**
#-dontwarn com.google.android.gms.ads.**
-dontwarn com.google.firebase.**
-keep class com.google.firebase.database.** { *; }

# fabric
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-printmapping mapping.txt
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#Event bus
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


#GreenDao
#-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
#public static java.lang.String TABLENAME;
#}
#-keep class **$Properties
#-dontwarn org.greenrobot.greendao.database.**
#
#-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
#    public static java.lang.String TABLENAME;
#}
#-keep class **$Properties


#GreenDao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties {*;}
-keep class de.greenrobot.** { *; }
-keep interface de.greenrobot.** { *; }
-keep class com.finopaytech.finosdk.models.sqlite.*{*;}

# If you do not use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do not use RxJava:
-dontwarn rx.**


#Morpho
-keep class com.morpho.** { *; }
-keep interface com.morpho.** { *; }

#Mantra
-keep class com.app.bhaskar.easypaisa.model.** {*;}

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }
-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName



#Proguard rules for razor pay
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}

-optimizations !method/inlining/*

-keepclasseswithmembers class * {
  public void onPayment*(...);
}