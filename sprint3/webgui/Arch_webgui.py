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
with Diagram('webguiArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_webgui', graph_attr=nodeattr):
          webgui=Custom('webgui','./qakicons/symActorWithobjSmall.png')
          hold_mock=Custom('hold_mock','./qakicons/symActorWithobjSmall.png')
          hold_updater=Custom('hold_updater','./qakicons/symActorWithobjSmall.png')
     sys >> Edge( label='hold_update', **evattr, decorate='true', fontcolor='darkgreen') >> webgui
     hold_mock >> Edge( label='hold_update', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     hold_updater >> Edge(color='magenta', style='solid', decorate='true', label='<reserve_slot<font color="darkgreen"> reserve_slot_success reserve_slot_fail</font> &nbsp; >',  fontcolor='magenta') >> hold_mock
     webgui >> Edge(color='magenta', style='solid', decorate='true', label='<get_hold_state<font color="darkgreen"> hold_state</font> &nbsp; >',  fontcolor='magenta') >> hold_mock
diag
