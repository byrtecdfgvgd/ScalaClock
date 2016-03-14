package View


import swing.event._
import swing.BorderPanel.Position._
import swing.Swing._
import swing._
import java.awt.{Color, Graphics2D, Point, geom}
import java.awt.{Graphics2D,Color,Font,BasicStroke}
import java.awt.geom._

object MainClockWindow extends SimpleSwingApplication {
  
   val timeText = new TextField {
     text = "00:00"
     
      focusable = true    
      listenTo(mouse.clicks, keys)
      
      reactions += {
        case e: MousePressed => requestFocus
        case KeyPressed(_, Key.Enter, _, _) =>
          nextCall(this.text)
      }
   }
  
   //used when accessed the first time
   lazy val ui = new Panel {
     background = Color.WHITE
     preferredSize = (200,200)
     
     focusable = true
     listenTo(mouse.clicks, mouse.moves, keys)
     
     var myClock: ClockModel = new ClockModel()
        
     reactions += {
       case e: MousePressed =>
         //moveTo(e.point)
         myClock.captureMouse(e.point)
         myClock.setFocus(e.point)         
         requestFocusInWindow()    
       case e: MouseDragged => 
         myClock.moveHand (e.point)
         myClock.setTimeString(e.point)
         timeText.text_=(myClock.time)
         timeText.repaint()
         myClock.captureMouse(e.point)
         repaint();
       case e: MouseReleased => //lineTo(e.point)
       case _: FocusLost => repaint()
     }
     
    def setClock(text: String) {
      if (text.length() == 5 && text.charAt(3) == ':')
      {
        println("out")
      }
    }
     
     override def paintComponent(g: Graphics2D) = {
       super.paintComponent(g);
       g.setColor(Color.BLACK)
       //clock border
       g.draw(new Ellipse2D.Double(25, 5, 150, 150))
       
       //minute hand
       g.draw(myClock.minute)
       
       //hour hand
       g.setStroke(new BasicStroke(3f))
       g.draw(myClock.hour)
     }
   }
   
  def nextCall(text: String) {
    ui.myClock.updateClock(text)
    ui.repaint()
  }
   
  def top = new MainFrame {
    title = "Clock Program"
    
    val timeLabel = new Label {
      text = "Time:"
      font = new Font("Cambria Math", java.awt.Font.BOLD, 20)
    }
    
   val viewPanel = new BorderPanel {
     layout(timeLabel) = Center
     layout(timeText) = South
     
   }
   
   
    contents = new BorderPanel {
      layout(ui) = West
      layout(viewPanel) = East
      
    }
    size = new Dimension(300, 200)
  }
  
}