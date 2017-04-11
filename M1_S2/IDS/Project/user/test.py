import pika
import user as u



connection = pika.BlockingConnection(pika.ConnectionParameters(
               'localhost'))
channel = connection.channel()


user = u.User("test1", [1, 3], channel)

# user.Call([1, 1])
