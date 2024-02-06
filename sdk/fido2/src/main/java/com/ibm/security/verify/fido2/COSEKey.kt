/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2

import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper


class COSEKey(kty: Int, alg: Int, crv: Int, x: ByteArray, y: ByteArray) {

    private val _kty = kty
    private val _alg = alg
    private val _crv = crv
    private val _x = x
    private val _y = y

    val toByteArray: ByteArray
        get() {
            val map = HashMap<Int, Any>()
            map[1] = _kty
            map[3] = _alg
            map[-1] = _crv
            map[-2] = _x
            map[-3] = _y

            return CBORMapper().writeValueAsBytes(map)
        }
}