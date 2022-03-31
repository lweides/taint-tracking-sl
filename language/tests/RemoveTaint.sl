function main() {
  t = addTaint("hello world");
  println(removeTaint(t, 0, 3));
  println(getTaint(t));
  println(isTainted(t));
  removeTaint(t, 3, 11);
  println(isTainted(t));
}