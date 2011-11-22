#
#
echo '{"messageType":4,"sharedList":[{"permissions":"r","path":"\/teste.txt", "size":22}],"deviceID":"numerorandom"}' | netcat -vv -c -u localhost 9764
