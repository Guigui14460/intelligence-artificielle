rm -rf bin
[ ! -d bin ] && mkdir bin
javac -d bin -cp lib/\* src/*/*.java