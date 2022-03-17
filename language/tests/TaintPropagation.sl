function main() {
  t1 = taint("hello") + taint(" world");
  t2 = taint("hello") + " world";
  t3 = "hello" + taint(" world");
  t4 = taint("hello") + 73;
  t5 = 73 + taint(" world");
  t6 = "hello" + 73;
  t7 = 73 + " world"; 
  t8 = "hello" + " world";
  
  println(isTainted(t1));
  println(isTainted(t2));
  println(isTainted(t3));
  println(isTainted(t4));
  println(isTainted(t5));
  println(isTainted(t6));
  println(isTainted(t7));
  println(isTainted(t8));
}