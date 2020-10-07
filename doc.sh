[ ! -d docs ] && mkdir docs
javadoc -d docs -cp lib/\* src/*.java src/*/*.java