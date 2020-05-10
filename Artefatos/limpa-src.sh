mkdir -p build
cd src
rm -rf *.o
file * | grep ELF | cut -d ":" -f1 | awk '{printf("strip %s\nmv %s ../build\n", $0, $0);}' > /tmp/limpa.sh
source /tmp/limpa.sh
cd ..
make clean
