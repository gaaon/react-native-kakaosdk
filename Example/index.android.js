/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    Image,
    Dimensions,
    NativeModules
} from 'react-native';

const {
    KakaoLoginManager
} = NativeModules;

import Button from 'react-native-button';
import colors from 'material-colors';

class Example extends Component {
    async handleClickKakaoButton(e) {
        e.preventDefault();

        try {
            let resp = await KakaoLoginManager.login();

            console.log(resp);
        }

        catch(e) {
            console.log(e);

        }

    }

    render() {
        return (
            <View style={styles.container}>
                <Button
                    onPress={this.handleClickKakaoButton.bind(this)}
                    containerStyle={styles.button}>
                    <View style={styles.iconWrapper}>
                        <Image source={require('./images/kakao_login_symbol.png')} style={{width:28, height: 25}}/>
                    </View>


                    <View  style={styles.textWrapper}>
                        <View style={styles.textContainer}>
                            <Text style={styles.text}>
                                카카오톡으로 로그인
                            </Text>
                        </View>
                    </View>
                </Button>
            </View>
        );
    }
}

const {width} = Dimensions.get('window');

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center'
    },
    button: {
        //alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: colors.yellow[400],
        borderRadius: 4,
        height: 60,
        elevation: 2,
        width: width/4*3,
        marginTop: 15
    },
    iconWrapper: {
        flex: 3,
        alignItems: 'center'
    },
    textWrapper: {
        flex: 7,
        borderLeftWidth: 3,
        alignItems: 'center',
        height: 40
    },
    textContainer: {
        flex: 1,
        //alignItems: 'center',
        justifyContent: 'center'
    },
    text: {
        fontSize: 15,
        fontWeight: 'bold',
        fontFamily: 'NanumGothic-ExtraBold'
    }
});

AppRegistry.registerComponent('Example', () => Example);
