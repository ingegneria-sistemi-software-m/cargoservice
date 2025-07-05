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
with Diagram('arch1Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctx_cargoservice', graph_attr=nodeattr):
          cargoservice=Custom('cargoservice','./qakicons/symActorWithobjSmall.png')
          cargorobot=Custom('cargorobot','./qakicons/symActorWithobjSmall.png')
          productservice=Custom('productservice','./qakicons/symActorWithobjSmall.png')
          hold=Custom('hold','./qakicons/symActorWithobjSmall.png')
          sonar=Custom('sonar','./qakicons/symActorWithobjSmall.png')
          external_client=Custom('external_client','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='container_arrived', **evattr, decorate='true', fontcolor='darkgreen') >> cargorobot
     cargorobot >> Edge( label='alarm', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sys >> Edge( label='riprendi_tutto', **evattr, decorate='true', fontcolor='darkgreen') >> cargorobot
     sonar >> Edge( label='container_arrived', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sonar >> Edge( label='interrompi_tutto', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     sonar >> Edge( label='riprendi_tutto', **eventedgeattr, decorate='true', fontcolor='red') >> sys
     cargorobot >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     external_client >> Edge(color='magenta', style='solid', decorate='true', label='<load_product<font color="darkgreen"> load_accepted load_refused</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<get_weight<font color="darkgreen"> get_weight_success get_weight_fail</font> &nbsp; >',  fontcolor='magenta') >> productservice
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<reserve_slot<font color="darkgreen"> reserve_slot_success reserve_slot_fail</font> &nbsp; >',  fontcolor='magenta') >> hold
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<handle_load_operation<font color="darkgreen"> load_operation_complete</font> &nbsp; >',  fontcolor='magenta') >> cargorobot
     cargorobot >> Edge(color='blue', style='solid',  decorate='true', label='<setdirection &nbsp; >',  fontcolor='blue') >> basicrobot
diag
