function main() {
  t = taint("hello world");
  removeTaint(t, 0, 3);
  println(isTainted(t));
  removeTaint(t, 3, 11);
  println(isTainted(t));
}