##1. Gradle 환경설정
https://developers.kakao.com/docs/android#gradle-환경설정

### 상세 설명
&lt;project-dir&gt;/android/build.properties에 다음 설정을 추가합니다.
```
subprojects {
    repositories {
        mavenCentral()
        maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
    }
}
```

&lt;project-dir&gt;/android/app/build.properties 파일의 dependencies 항목 아래에 다음 설정을 추가합니다.
```
compile group: 'com.kakao.sdk', name: 'kakaotalk', version: project.KAKAO_SDK_VERSION
```
따라서 아래와 같은 모습이 됩니다.
```
dependencies {
    compile fileTree(dir: "libs", include: ["*.jar"])
    compile "com.android.support:appcompat-v7:23.0.1"
    compile "com.facebook.react:react-native:+"  // From node_modules
    ...
    compile group: 'com.kakao.sdk', name: 'kakaotalk', version: project.KAKAO_SDK_VERSION

}
```

&lt;project-dir&gt;/android/app/gradle.properties 파일에 다음 설정을 추가합니다. 파일이 없을시 새로 생성하시면 됩니다.
```
KAKAO_SDK_VERSION=1.1.21
```

##2. 앱 설정
https://developers.kakao.com/docs/android#시작하기-앱-생성
