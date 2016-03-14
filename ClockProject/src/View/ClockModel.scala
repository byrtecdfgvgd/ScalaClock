package View

import java.awt.{Color, Graphics2D, Point, geom}
import java.awt.geom._

class ClockModel (){
      var count : Int = 0
      var numRounds: Int = 0   
  
     var centerX : Int = 100
     var centerY : Int = 80
     
     var hourAngle : Double = 270.0
     var minuteAngle : Double = 270.0
     var hourLength : Int = 40
     var minuteLength : Int = 75

     var mouse: Point = new Point(100, 80);
     
     var time: String = "00:00" 
      
     //could have made these points
     var mX : Double = centerX + Math.cos(Math.toRadians(minuteAngle)) * minuteLength
     var mY : Double = centerY + Math.sin(Math.toRadians(minuteAngle)) * minuteLength
     var hX : Double = centerX + Math.cos(Math.toRadians(hourAngle)) * hourLength
     var hY : Double = centerY + Math.sin(Math.toRadians(hourAngle)) * hourLength
  
     var minute = new Line2D.Double(centerX, centerY, mX, mY)
     var hour = new Line2D.Double(centerX, centerY, hX, hY)
     
     var minuteFocus : Boolean = false
     var hourFocus : Boolean = false

     var prevMinAngle: Double = 0
     
     def updateClock(text: String) {
       if (text.length() == 5 && text.charAt(2) == ':')
       {
         var hr: Int = text.substring(0, 2).toInt
         var min: Int = text.substring(3, 5).toInt

         minuteAngle = (min/60.0 * 360) - 90
         mX = centerX + Math.cos(Math.toRadians(minuteAngle)) * minuteLength
         mY = centerY + Math.sin(Math.toRadians(minuteAngle)) * minuteLength
         //new minute hand
         minute = new Line2D.Double(centerX, centerY, mX, mY)         
         println(minuteAngle)
         hourAngle = (hr/12.0 * 360) - 90 + ((minuteAngle + 90)/12)
         hX = centerX + Math.cos(Math.toRadians(hourAngle)) * hourLength
         hY = centerY + Math.sin(Math.toRadians(hourAngle)) * hourLength
         //new Hour Hand
         hour = new Line2D.Double(centerX, centerY, hX, hY)  
       }
     }
     
     def setTimeString(mousePoint: Point) {
       var curMin: Int = (((minuteAngle)/360*60 + 15) % 60).toInt
       var curHr: Int = (math.floor(hourAngle/360*12) + 3).toInt % 24
       
       if (curHr < 0)
       {
         curHr += 24 
       }
       
       time = curHr + ":" + curMin
     }
     
     def setFocus(mousePoint: Point) {
         var minDist : Double =  distMouseToHands(new Point(centerX, centerY), new Point (mX.toInt, mY.toInt), mousePoint)
         var hourDist : Double =  distMouseToHands(new Point(centerX, centerY), new Point (hX.toInt, hY.toInt), mousePoint)
         
         if (minDist < hourDist)
         {
           minuteFocus = true
           hourFocus = false
         }
         else
         {
           minuteFocus = false
           hourFocus = true
         }       
     }
     
     
     def moveHand(mousePoint: Point) {
       if (minuteFocus)
       {
         //setting up minute hand
         minuteAngle = math.toDegrees(math.atan2((centerY - mousePoint.y), (centerX - mousePoint.x))) + 180
         mX = centerX + Math.cos(Math.toRadians(minuteAngle)) * minuteLength
         mY = centerY + Math.sin(Math.toRadians(minuteAngle)) * minuteLength
         //new minute hand
         minute = new Line2D.Double(centerX, centerY, mX, mY)       
         
         //calculating the number of times the minute hand has looped the clock
         if (prevMinAngle > 350 && minuteAngle < 10 && (minuteAngle - prevMinAngle) < 0)
         {
           numRounds = numRounds + 1
         }
         if (prevMinAngle < 5 && minuteAngle > 355 && (minuteAngle - prevMinAngle) > 0)
         {
           numRounds = numRounds - 1
         }

         prevMinAngle = minuteAngle
         
         //calculating new hour hand position
         hourAngle = numRounds * 30
         if ((minuteAngle - prevMinAngle) <= 0)
         {
             hourAngle = (hourAngle - 90) + (minuteAngle - 270)/12 
         }
         
         hX = centerX + Math.cos(Math.toRadians(hourAngle)) * hourLength
         hY = centerY + Math.sin(Math.toRadians(hourAngle)) * hourLength
         //new Hour Hand
         hour = new Line2D.Double(centerX, centerY, hX, hY)  
       }
       //we are moving the hour hand
       else if (hourFocus)
       {
         var oldMouseAngle: Double = math.toDegrees(math.atan2((centerY - mouse.y), (centerX - mouse.x))) + 180
         var newMouseAngle: Double = math.toDegrees(math.atan2((centerY - mousePoint.y), (centerX - mousePoint.x))) + 180

         if ((newMouseAngle > oldMouseAngle) || (newMouseAngle < 5 && oldMouseAngle > 355))
         {
           if (count > 20)
           {
             numRounds = numRounds + 1
             count = 0;
           }
           else
           {
             count += 1
           }
         }
         else
         {
           if (count > 20)
           {
             numRounds = numRounds - 1
             count = 0
           }
           else
           {
             count += 1
           }
         }
         
         //calculating new hour hand position
         hourAngle = numRounds * 30
         if ((minuteAngle - prevMinAngle) <= 0)
         {
             hourAngle = (hourAngle - 90) + (minuteAngle - 270)/12 
         }
         
         hX = centerX + Math.cos(Math.toRadians(hourAngle)) * hourLength
         hY = centerY + Math.sin(Math.toRadians(hourAngle)) * hourLength
         //new Hour Hand
         hour = new Line2D.Double(centerX, centerY, hX, hY)         
       }
     }
    
     def captureMouse(p: Point) {
       mouse = p;
     }
     
     //used for finding distance of mouse to each hand
     def dotProduct(a: Point, b: Point, c: Point) : Double = {
       var ab: Point = new Point(b.x - a.x, b.y - a.y)
       var bc: Point = new Point(c.x - b.x, c.y - b.y)
       
      var dot : Double = ab.x * bc.x + ab.y * bc.y
      
      return dot
     }
     
     def crossProduct(a: Point, b: Point, c: Point) : Double = {
       var ab: Point = new Point(b.x - a.x, b.y - a.y)
       var ac: Point = new Point(c.x - a.x, c.y - a.y)
       
       var cross : Double = ab.x * ac.y - ab.y * ac.x
       
       return cross
     }
     
     def dist(a: Point, b: Point) : Double = {
       var d1: Double = a.x - b.x
       var d2: Double = a.y - b.y
       
       return Math.sqrt(Math.pow(d1, 2.0) + Math.pow(d2, 2.0))       
     }
     
     //computes AB to point C
     def distMouseToHands(a: Point, b: Point, c: Point) : Double = {
       var distance: Double = crossProduct(a, b, c)/dist(a, b)
       
       var dot1: Double = dotProduct(a, b, c) 
       if ( dot1 > 0.0) {
         return dist(b, c)
       }
      
       var dot2: Double = dotProduct(b, a, c)
       if (dot2 > 0) {
         return dist(a, c)
       }
       
       return Math.abs(distance)
     }
}