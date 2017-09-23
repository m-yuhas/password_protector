#!/usr/bin/python

from json import loads, dumps
from sys import argv

class cipher:
    def __init__(self,mode,initial_value):
        if mode not in ['ECB','CBC','CFB']:
            raise ValueError('Only modes ECB, CBC, and CFB supported')
        self.cipher_signature = 0x48534946
        if mode != 'ECB':
            return ""

def table_op(operation):
    if operation == 'TAB_DISABLE':
        tab_enable = 0
    elif operation == 'TAB_ENABLE':
        tab_enable = 1
    elif operation == 'TAB_RESET':
        query_count = 0
        for tab in tabs:
            tab = None
    elif operation == 'TAB_QUERY':
        query_count += 1
        for tab in tabs:
            if tab != 'ALL_USED':
                return False
        if query_count < 50:
            return False
    else:
        return True





def rsa_encrypt(plain_text):
    return ""

def rsa_decrypt(cypher_text):
    return ""

def twofish_encrypt(plain_text):
    return ""

def twofish_decrypt(cypher_text):
    return ""

def generate_password(length,upper,lower,number,symbol):
    return ""

if __name__ == '__main__':
    """
    if len(argv) != 2:
        print "Usage: python password_protector <password file>"
    with open(argv[1],'r+') as fp:
        encrypted_text = fp.read()
    """
