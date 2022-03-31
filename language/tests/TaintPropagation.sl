function main() {
  t1 = addTaint("hello", 1) + addTaint(" world", 2);
  t2 = addTaint("hello") + " world";
  t3 = "hello" + addTaint(" world");
  t4 = addTaint("hello") + 73;
  t5 = 73 + addTaint(" world");
  t6 = "hello" + 73;
  t7 = 73 + " world"; 
  t8 = "hello" + " world";
  
  println(isTainted(t1));
  println(getTaint(t1));
  println(isTainted(t2));
  println(getTaint(t2));
  println(isTainted(t3));
  println(getTaint(t3));
  println(isTainted(t4));
  println(getTaint(t4));
  println(isTainted(t5));
  println(getTaint(t5));
  println(isTainted(t6));
  println(getTaint(t6));
  println(isTainted(t7));
  println(getTaint(t7));
  println(isTainted(t8));
  println(getTaint(t8));
}