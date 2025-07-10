### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('iodevicesArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_iodevices', graph_attr=nodeattr):
          sonardevice=Custom('sonardevice','./qakicons/symActorWithobjSmall.png')
          measure_processor=Custom('measure_processor','./qakicons/symActorWithobjSmall.png')
          led=Custom('led','./qakicons/symActorWithobjSmall.png')
          sonar_listener=Custom('sonar_listener','./qakicons/symActorWithobjSmall.png')
     sonardevice >> Edge( label='measurement', **eventedgeattr, decorate='true', fontcolor='red') >> measure_processor
     measure_processor >> Edge( label='riprendi_tutto', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     measure_processor >> Edge( label='container_arrived', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     measure_processor >> Edge( label='container_absent', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     measure_processor >> Edge( label='interrompi_tutto', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='interrompi_tutto', **evattr, decorate='true', fontcolor='darkgreen') >> led
     sys >> Edge( label='riprendi_tutto', **evattr, decorate='true', fontcolor='darkgreen') >> led
     sys >> Edge( label='container_arrived', **evattr, decorate='true', fontcolor='darkgreen') >> sonar_listener
     sys >> Edge( label='container_absent', **evattr, decorate='true', fontcolor='darkgreen') >> sonar_listener
     sys >> Edge( label='interrompi_tutto', **evattr, decorate='true', fontcolor='darkgreen') >> sonar_listener
     sys >> Edge( label='riprendi_tutto', **evattr, decorate='true', fontcolor='darkgreen') >> sonar_listener
diag
