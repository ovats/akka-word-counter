package playground

object SomeTests extends App{

  def prueba(param: Int) = {
    if (param == 0) {
      None
    } else if (param==1){
      Some("1")
    } else {
      Some(true)
    }
  }

}
