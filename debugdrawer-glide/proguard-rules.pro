# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/palaima/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
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

# GlideModule uses reflection to access memoryCache field in Glide instance
# and if proguard is enabled(release build for example) an exception is thrown
# that no such field exists because the field was obfuscated
-keepclassmembers class com.bumptech.glide.Glide {
    private com.bumptech.glide.load.engine.cache.MemoryCache memoryCache;
}
