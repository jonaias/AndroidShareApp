#
#Waits for 3s before closing the connection
echo 'numerorandom /teste.txt' | netcat -vv -w 3 localhost 23753
