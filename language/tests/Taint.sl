function main() {
  t1 = addTaint("hello", "world");
  println(isTainted(t1));
  println(getTaint(t1));

  t2 = addTaint("number", 73);
  println(isTainted(t2));
  println(getTaint(t2));
}