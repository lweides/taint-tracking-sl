function main() {
  t = addTaint("hello world");
  println(isTainted(t));
  tRemoved = removeTaint(t, 0, 3);
  println(getTaint(tRemoved));
  println(getTaint(t));
  tRemoved = removeTaint(tRemoved, 3, 11);
  println(getTaint(tRemoved));
  println(isTainted(tRemoved));
}