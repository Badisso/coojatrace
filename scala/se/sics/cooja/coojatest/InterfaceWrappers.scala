package se.sics.cooja.coojatest.interfacewrappers



import se.sics.cooja._
import se.sics.cooja.coojatest.wrappers._

import se.sics.cooja.interfaces._

import scala.collection.JavaConverters._



/**
 * Implicit conversions from original cooja mote interfaces to their rich wrappers.
 */
object Conversions {
  /**
   * Cast interface to specified subtype.
   *
   * '''Note:''' This function only serves to shorten the actual conversion functions below.
   *
   * @param i [[MoteInterface]] to be casted (must be supertype)
   * @return interface cast to specified subtype of [[MoteInterface]]
   * @tparam T interface type to cast to (subtype of [[MoteInterface]])
   */
  private def interface[T <: MoteInterface](i: MoteInterface): T = i.asInstanceOf[T]
  
  implicit def led2RichLED(i: LED)(implicit sim: Simulation) = new RichLED(i, sim)
  implicit def ledInterface = interface[LED] _

  implicit def radio2RichRadio(r: Radio)(implicit sim: Simulation) = new RichRadio(r, sim)
  implicit def radioInterface = interface[Radio] _

  implicit def position2RichPosition(p: Position)(implicit sim: Simulation) = 
    new RichPosition(p, sim)
  implicit def positionInterface = interface[Position] _

  implicit def log2RichLog(l: Log)(implicit sim: Simulation) = new RichLog(l, sim)
  implicit def logInterface = interface[Log] _

  implicit def ipAddress2RichIPAddress(ia: IPAddress)(implicit sim: Simulation) = 
    new RichIPAddress(ia, sim)
  implicit def ipAddressInterface = interface[IPAddress] _

  implicit def rimeAddress2RichRimeAddress(ra: RimeAddress)(implicit sim: Simulation) = 
    new RichRimeAddress(ra, sim)
  implicit def rimeAddressInterface = interface[RimeAddress] _

  implicit def moteID2RichMoteID(id: MoteID)(implicit sim: Simulation) = new RichMoteID(id, sim)
  implicit def moteIDInterface = interface[MoteID] _

  implicit def beeper2RichBeeper(b: Beeper)(implicit sim: Simulation) = new RichBeeper(b, sim)
  implicit def beeperInterface = interface[Beeper] _

  implicit def button2RichButton(bt: Button)(implicit sim: Simulation) = new RichButton(bt, sim)
  implicit def buttonInterface = interface[Button] _
}



/**
 * Accessor functions for mote interfaces (only to reduce typing).
 * 
 * '''Note:''' This trait is mixed into [[RichMote]].
 */
trait InterfaceAccessors { this: RichMote =>
  /**
   * Get map of mote interfaces with interface classnames as keys.
   * @return map of (clasnsname -> interface) elements
   */
  def interfaces: Map[String, MoteInterface] = 
    mote.getInterfaces.getInterfaces.asScala.map(i => i.getClass.getName.split("\\.").last -> i).toMap
  
  /**
   * Get interface of specified class.
   *
   * @param t class of interface type
   * @return interface cast to specified subtype of [[MoteInterface]]
   * @tparam T interface type to return (subtype of [[MoteInterface]])
   */
  def interface[T <: MoteInterface](t: Class[T]): T = mote.getInterfaces.getInterfaceOfType(t)
  
  def led = interface(classOf[LED])
  def radio = interface(classOf[Radio])
  def position = interface(classOf[Position])
  def log = interface(classOf[Log])
  def ipAddress = interface(classOf[IPAddress])
  def rimeAddress = interface(classOf[RimeAddress])
  def moteID = interface(classOf[MoteID])
  def beeper = interface(classOf[Beeper])
  def button = interface(classOf[Button])
  def clock = interface(classOf[Clock])
  def pir = interface(classOf[PIR])
}



/**
 * Mote LED status.
 * 
 * @param redOn `true` if red LED is lit 
 * @param greenOn `true` if green LED is lit 
 * @param yellowOn `true` if yellow LED is lit 
 */
case class LEDStatus(redOn: Boolean, greenOn: Boolean, yellowOn: Boolean)

/**
 * Wrapper for mote LED interface.
 */
class RichLED(val interface: LED, val simulation: Simulation) extends RichInterface[LED] {
  /**
   * Get signal of LED status.
   * @return [[Signal]] of type [[LEDStatus]]
   */
  lazy val status = observedSignal {
    LEDStatus(interface.isRedOn, interface.isGreenOn, interface.isYellowOn) 
  }
}



/**
 * Mote position.
 * 
 * @param x X coordinate of mote 
 * @param y Y coordinate of mote
 * @param z Z coordinate of mote 
 */
case class MotePosition(x: Double, y: Double, z: Double)

/**
 * Wrapper for mote position (interface).
 */
class RichPosition(val interface: Position, val simulation: Simulation) extends RichInterface[Position] {
  /**
   * Get signal of mote position.
   * @return [[Signal]] of type [[MotePosition]]
   */
  lazy val position = observedSignal {
    MotePosition(interface.getXCoordinate, interface.getYCoordinate, interface.getZCoordinate)
  }
}



/**
 * Wrapper for mote radio interface.
 */
class RichRadio(val interface: Radio, val simulation: Simulation) extends RichInterface[Radio]  {
  /**
   * Get eventstream of mote radio interface events.
   * @return [[EventStream]] of radio events
   */
  lazy val events = observedEvent { interface.getLastEvent }

  /**
   * Get signal of mote radio interference status.
   * @return boolean [[Signal]] of interference status, `true` when being interfered
   */
  lazy val interfered = observedSignal { interface.isInterfered }
  
  /**
   * Get signal of mote radio receiver status.
   * @return boolean [[Signal]] of receiver status, `true` when receiver is on
   */
  lazy val receiverOn = observedSignal { interface.isReceiverOn }

  /**
   * Get signal of mote radio reception status.
   * @return boolean [[Signal]] of reception status, `true` when receiving
   */
  lazy val receiving = observedSignal { interface.isReceiving }

  /**
   * Get signal of mote radio transmission status.
   * @return boolean [[Signal]] of transmission status, `true` when transmissing
   */
  lazy val transmitting = observedSignal { interface.isTransmitting }
  


  /**
   * Get signal of mote radio channel.
   * @return [[Signal]] of radio channel
   */
  lazy val channel = observedSignal { interface.getChannel }

  /**
   * Get signal of mote radio output power.
   * @return [[Signal]] of output power.
   */
  lazy val currentOutputPower = observedSignal { interface.getCurrentOutputPower }

  /**
   * Get signal of mote radio output power indicator.
   * @return [[Signal]] of output power indicator
   */
  lazy val currentOutputPowerIndicator = observedSignal { interface.getCurrentOutputPowerIndicator }

  /**
   * Get signal of mote radio signal strength.
   * @return [[Signal]] of current signal strength
   */
  lazy val currentSignalStrength = observedSignal { interface.getCurrentSignalStrength }



  /**
   * Get signal of mote radio position.
   * @return [[Signal]] of type [[MotePosition]]
   */
  lazy val position = observedSignal { interface.getPosition }

  // TODO: only fire at right event
  //lazy val packetsTransmitted = observedSignal { interface.getLastPacketTransmitted }
  //lazy val packetsReceived = observedSignal { interface.getLastPacketReceived } 
}



/**
 * Wrapper for mote log (interface).
 */
class RichLog(val interface: Log, val simulation: Simulation) extends RichInterface[Log] {
  /**
   * Get eventstream of log messages.
   * @return [[EventStream]] of log messages
   */
  lazy val messages = observedEvent { interface.getLastLogMessage }
}



/**
 * Wrapper for mote beeper (interface).
 */
class RichBeeper(val interface: Beeper, val simulation: Simulation) extends RichInterface[Beeper] {
  /**
   * Get signal of mote beeper status.
   * @return boolean [[Signal]] of beeper status, `true` when beeping
   */
  lazy val beeping = observedSignal { interface.isBeeping }
}



/**
 * Wrapper for mote button (interface).
 */
class RichButton(val interface: Button, val simulation: Simulation) extends RichInterface[Button] {
  /**
   * Get signal of mote button status.
   *
   * '''Note:''' Some mote types will notify observers when clicked, but do not set
   * pressed status to `true`, in these cases signal will always be `false` but "changes"
   * are still propagated.
   *
   * @return boolean [[Signal]] of button status, `true` when pressed
   */
  lazy val pressed = observedSignal { interface.isPressed }
}



/**
 * Wrapper for mote IP address (interface).
 */
class RichIPAddress(val interface: IPAddress, val simulation: Simulation) extends RichInterface[IPAddress] {
  /**
   * Get signal of mote IP address.
   * @return [[Signal]] of mote IP address as string
   */
  lazy val ipAddress = observedSignal { interface.getIPString }
}



/**
 * Wrapper for mote rime address (interface).
 */
class RichRimeAddress(val interface: RimeAddress, val simulation: Simulation) extends RichInterface[RimeAddress] {
  /**
   * Get signal of mote rime address.
   * @return [[Signal]] of mote rime address as string
   */
  lazy val address = observedSignal { interface.getAddressString }
}



/**
 * Wrapper for mote ID (interface).
 */
class RichMoteID(val interface: MoteID, val simulation: Simulation) extends RichInterface[MoteID] {
  /**
   * Get signal of mote ID.
   * @return [[Signal]] of mote ID as integer
   */
  lazy val moteID = observedSignal { interface.getMoteID }
}
