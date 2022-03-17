function main() {
  t1 = taint("hello", "world");
  println(isTainted(t1));
  println(getTaint(t1, 0));

  t2 = taint("number", 73);
  println(isTainted(t2));
  println(getTaint(t2, 0));
}