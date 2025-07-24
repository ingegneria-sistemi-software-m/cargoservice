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
with Diagram('webguimodelArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_webgui', graph_attr=nodeattr):
          webgui=Custom('webgui','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctx_cargoservice', graph_attr=nodeattr):
          cargoservice=Custom('cargoservice(ext)','./qakicons/externalQActor.png')
          hold=Custom('hold(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='hold_update', **evattr, decorate='true', fontcolor='darkgreen') >> webgui
     webgui >> Edge(color='magenta', style='solid', decorate='true', label='<get_hold_state<font color="darkgreen"> hold_state</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     hold >> Edge(color='blue', style='solid',  decorate='true', label='<hold_update &nbsp; >',  fontcolor='blue') >> webgui
diag
