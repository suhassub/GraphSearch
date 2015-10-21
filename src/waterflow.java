import java.io.*;
import java.lang.*;
import java.lang.Boolean;
import java.lang.Exception;
import java.lang.Integer;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;


public class waterflow{

   public static void main(String []args)
        {
            long start=System.currentTimeMillis();
            File file =null;
            PrintWriter writefile=null;
            if(args.length>0) {
                file = new File(args[1]);
            }
            try {
                BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\vatsa\\IdeaProjects\\waterflow\\src\\sampleInput.txt"));
                writefile=new PrintWriter(new FileWriter("output.txt"));
                String line;
                Integer numOfTestCases;
                line = br.readLine();
                if (line != null) {
                    numOfTestCases = Integer.valueOf(line);
                } else {
                    return;
                }
                int i = 0;
                List<Edge> edgesList = new ArrayList<>();
                while (i < numOfTestCases) {
                    Edge edges = new Edge();
                    int countLines = 0;
                    while ((line = br.readLine()) != null) {
                        if (line.equals("") && countLines!=3) {
                            countLines = 0;
                            break;

                        } else {
                            if (countLines == 0) {
                                if(line.equals("BFS")||line.equals("DFS")||line.equals("UCS")) {
                                    edges.Algo = line;
                                }
                            } else if (countLines == 1) {
                                edges.SourceNode = line;
                                edges.existingNodes.put(line,line);
                            } else if (countLines == 2) {
                                String[] destArray = line.split(" ");
                                for (int j = 0; j < destArray.length; j++) {
                                    edges.DestinationNodes.put(destArray[j], 1);
                                    edges.existingNodes.put(destArray[j],destArray[j]);
                                }

                            } else if (countLines == 3) {
                                String[] destArray = line.split(" ");
                                for (int j = 0; j < destArray.length; j++) {

                                    edges.existingNodes.put(destArray[j],destArray[j]);
                                }
                                //countLines++;
                                //continue;
                            } else if (countLines == 4) {
                                edges.NumOfPipes = Integer.valueOf(line);
                            } else {
                                if (line.split(" ").length == 1) {
                                    edges.StartTime = Integer.valueOf(line);
                                } else {
                                    String[] adjArray = line.split(" ");
                                    if(edges.existingNodes.containsKey(adjArray[0]) && edges.existingNodes.containsKey(adjArray[1])) {
                                        Node nodeDetails = new Node();
                                        nodeDetails.Parent = adjArray[0];
                                        nodeDetails.State = adjArray[1];
                                        nodeDetails.PathCost = Integer.valueOf(adjArray[2]);

                                        if ((Integer.valueOf(adjArray[3]) == 0)) {
                                            nodeDetails.offTimes = Boolean.FALSE;

                                        } else {
                                            nodeDetails.offTimes = Boolean.TRUE;
                                            for (int k = 4; k < adjArray.length; k++) {
                                                String times = adjArray[k];
                                                String[] timeArray = times.split("-");
                                                nodeDetails.Time.put(timeArray[0], timeArray[1]);
                                            }
                                        }

                                        if (edges.AdjList.containsKey(adjArray[0])) {
                                            List<Node> nodeList = edges.AdjList.get(adjArray[0]);
                                            nodeList.add(nodeDetails);
                                            edges.AdjList.put(adjArray[0], nodeList);
                                        } else {
                                            List<Node> nodeList = new ArrayList<>();
                                            nodeList.add(nodeDetails);
                                            edges.AdjList.put(adjArray[0], nodeList);
                                        }
                                    }


                                }




                            }
                                countLines++;
                        }
                    }


                    i++;
                    edgesList.add(edges);
                }
                for (Edge eachEdge : edgesList) {
                    if (eachEdge.Algo.equals("BFS")) {
                        BreadthFirstSearch(eachEdge,writefile);
                    }
                    if (eachEdge.Algo.equals("DFS")) {
                        DepthFirstSearch(eachEdge,writefile);
                    }
                    if (eachEdge.Algo.equals("UCS")) {
                        UniformCostSearch(eachEdge,writefile);
                    }
                }
                writefile.flush();
                writefile.close();
                long end=System.currentTimeMillis();
                System.out.println(end-start);
            }
            catch (Exception ex) {
                if (writefile != null) {
                    writefile.flush();
                    writefile.close();
                }

                ex.printStackTrace();
            }

        }



    private static Boolean DepthFirstSearch(Edge edges,PrintWriter writeFile) {
        try {
            Node State = new Node();
            State.State = edges.SourceNode;
            if (edges != null && edges.DestinationNodes.size() > 0 && edges.DestinationNodes.containsKey(State.State)) {
                writeFile.println(State.State + " " + 0);
                //writeFile.flush();
                return Boolean.TRUE;
            } else {
                Stack<Node> frontier = new Stack<>();
                Deque<Node> explored = new ArrayDeque<>();
                frontier.push(State);
                int pathCost;
                while (true) {
                    if (frontier.isEmpty()) {
                        writeFile.println("None");
                        //writeFile.flush();
                        return Boolean.FALSE;
                    } else {
                        Node firstnode = frontier.pop();
                        firstnode.Visited = Boolean.TRUE;
                        explored.add(firstnode);
                        if (edges != null && edges.DestinationNodes.size() > 0 && edges.DestinationNodes.containsKey(firstnode.State)) {
                            String temp = firstnode.Parent;
                            pathCost = 0;
                            int flag = 0;
                            while (true) {
                                for (Node explorednode : explored) {
                                    if (explorednode.State.equals(temp)) {
                                        pathCost += 1;
                                        temp = explorednode.Parent;
                                        if (temp.equals("") || temp == null) {
                                            flag = 1;
                                        }
                                        break;
                                    }
                                }
                                if (flag == 1) {
                                    break;
                                }
                            }
                            pathCost = edges.StartTime + pathCost;
                            writeFile.println(firstnode.State + " " + pathCost % 24);
                            //writeFile.flush();
                            return Boolean.TRUE;

                        }
                        if (edges.AdjList.containsKey(firstnode.State)) {
                            List<Node> nodes = edges.AdjList.get(firstnode.State);
                            List<String> nodeNamesList = SortChildReverseAplhabetically(nodes);
                            for (String s : nodeNamesList) {
                                for (Node node : nodes) {

                                    if (s.equals(node.State)) {

                                        int exploredFlag = 0;
                                        for (Node explorednode : explored) {
                                            if (explorednode.State.equals(node.State)) {
                                                {
                                                    exploredFlag = 1;
                                                    break;
                                                }
                                            }
                                        }
                                        if (exploredFlag == 0) {
                                            frontier.push(node);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            writeFile.println("None");
            //writeFile.flush();
            return Boolean.FALSE;
        }
    }

    private static Boolean UniformCostSearch(Edge edges,PrintWriter writeFile) {
        try {
            Node State = new Node();
            State.State = edges.SourceNode;
            State.PathCost = 0;
            State.ParentPathCost = 0;
            int pathCost = 0;
            PriorityQueue<Node> frontier = new PriorityQueue<>(100, new PriorityCompare());
            frontier.offer(State);
            Deque<Node> explored = new ArrayDeque<>();
            while (true) {
                if (frontier.isEmpty()) {
                    writeFile.println("None");
                    //writeFile.flush();
                    return Boolean.FALSE;
                } else {
                    Node node = frontier.poll();
                    explored.add(node);
                    if (edges != null && edges.DestinationNodes.size() > 0 && edges.DestinationNodes.containsKey(node.State)) {
                        writeFile.println(node.State + " " + (edges.StartTime + node.PathCost) % 24);
                        //writeFile.flush();
                        return Boolean.TRUE;
                    }
                    if (edges.AdjList.containsKey(node.State)) {
                        List<Node> nodesList = edges.AdjList.get(node.State);
                        for (Node newnode : nodesList) {

                            int PathCostOfEach = CalculateParentPathCost(newnode, explored);
                            int ParentPathCost = PathCostOfEach;
                            int PathCost = newnode.PathCost + ParentPathCost;
                            //newnode.ParentPathCost=PathCostOfEach;
                            //newnode.PathCost=newnode.PathCost+newnode.ParentPathCost;
                            int exploredFlag = ChecknodeisPresentInQueue(explored, newnode);

                            int frontierFlag = 0;
                            for (Node queueNode : frontier) {
                                //if (queueNode.State.equals(newnode.State) && PathCost == queueNode.PathCost)
                                if (queueNode.State.equals(newnode.State)) {
                                    frontierFlag = 1;
                                    break;
                                }
                            }
                            if (frontierFlag != 1 && exploredFlag != 1) {
                                if (!newnode.offTimes) {
                                    newnode.PathCost = PathCost;
                                    newnode.ParentPathCost = ParentPathCost;
                                    frontier.offer(newnode);
                                } else {
                                    Iterator<Map.Entry> it = newnode.Time.entrySet().iterator();
                                    int flag = 0;
                                    while (it.hasNext()) {
                                        Map.Entry pair = (Map.Entry) it.next();
                                        if (((edges.StartTime + ParentPathCost) % 24 >= Integer.parseInt(pair.getKey().toString())) && ((edges.StartTime + ParentPathCost) % 24 <= Integer.parseInt(pair.getValue().toString()))) {
                                            flag = 1;
                                            break;
                                        }
                                    }
                                    if (flag != 1) {
                                        newnode.PathCost = PathCost;
                                        newnode.ParentPathCost = ParentPathCost;
                                        frontier.offer(newnode);
                                    }
                                }
                            }
                            else if (frontierFlag==1)
                            {
                                int frontierPathCost=CalculateFrontierPathCost(frontier,newnode);
                                if(PathCost<frontierPathCost)
                                {
                                    if (!newnode.offTimes) {
                                        newnode.PathCost = PathCost;
                                        newnode.ParentPathCost = ParentPathCost;
                                        ReplaceFrontierNode(frontier, newnode);
                                    } else {
                                        Iterator<Map.Entry> it = newnode.Time.entrySet().iterator();
                                        int flag = 0;
                                        while (it.hasNext()) {
                                            Map.Entry pair = (Map.Entry) it.next();
                                            if (((edges.StartTime + ParentPathCost) % 24 >= Integer.parseInt(pair.getKey().toString())) && ((edges.StartTime + ParentPathCost) % 24 <= Integer.parseInt(pair.getValue().toString()))) {
                                                flag = 1;
                                                break;
                                            }
                                        }
                                        if (flag != 1) {
                                            newnode.PathCost = PathCost;
                                            newnode.ParentPathCost = ParentPathCost;
                                            ReplaceFrontierNode(frontier,newnode);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            writeFile.println("None");
            //writeFile.flush();
            return Boolean.FALSE;
        }
    }

        private static  int CalculateFrontierPathCost(PriorityQueue<Node> frontier, Node newnode)
        {
            for(Node node:frontier)
            {
                if(newnode.State.equals(node.State))
                {
                    return node.PathCost;
                }
            }
            return 0;
        }

        private static PriorityQueue<Node> ReplaceFrontierNode(PriorityQueue<Node> frontier, Node newnode)
        {
            for(Node node:frontier) {
                if(newnode.State.equals(node.State))
                {
                    frontier.remove(node);
                    frontier.offer(newnode);
                    break;
                }
            }
            return frontier;
        }


        private static Boolean BreadthFirstSearch(Edge edges,PrintWriter writeFile) {
            try {
                Node State = new Node();
                State.State = edges.SourceNode;
                if (edges != null && edges.DestinationNodes.size() > 0 && edges.DestinationNodes.containsKey(State.State)) {
                    writeFile.println(State.State + " " + 0);
                    //writeFile.flush();
                    return Boolean.TRUE;
                } else {
                    Deque<Node> frontier = new ArrayDeque<>();
                    Deque<Node> explored = new ArrayDeque<>();
                    frontier.add(State);
                    int pathCost;
                    while (true) {
                        if (frontier.isEmpty()) {
                            writeFile.println("None");
                            //writeFile.flush();
                            return Boolean.FALSE;
                        } else {
                            Node firstnode = frontier.remove();
                            firstnode.Visited = Boolean.TRUE;
                            explored.add(firstnode);
                            if (edges.AdjList.containsKey(firstnode.State)) {

                                List<Node> nodes = edges.AdjList.get(firstnode.State);
                                List<String> nodeNamesList = SortChildAplhabetically(nodes);
                                for (String s : nodeNamesList) {
                                    for (Node node : nodes) {
                                        if (s.equals(node.State)) {
                                            int exploredFlag = ChecknodeisPresentInQueue(explored, node);

                                            int frontierFlag = ChecknodeisPresentInQueue(frontier, node);

                                            if (frontierFlag != 1 && exploredFlag != 1) {
                                                if (edges != null && edges.DestinationNodes.size() > 0 && edges.DestinationNodes.containsKey(node.State)) {
                                                    String temp = node.Parent;
                                                    pathCost = 0;
                                                    int flag = 0;
                                                    while (true) {
                                                        for (Node explorednode : explored) {
                                                            if (explorednode.State.equals(temp)) {
                                                                pathCost += 1;
                                                                temp = explorednode.Parent;
                                                                if (temp.equals("") || temp == null) {
                                                                    flag = 1;
                                                                }
                                                                break;
                                                            }
                                                        }
                                                        if (flag == 1) {
                                                            break;
                                                        }
                                                    }
                                                    //pathCost=edges.StartTime+node.Depth;
                                                    pathCost = edges.StartTime + pathCost;
                                                    writeFile.println(node.State + " " + pathCost % 24);
                                                    //writeFile.flush();
                                                    return Boolean.TRUE;
                                                }
                                                frontier.add(node);
                                            }
                                        }
                                    }

                                }


                            }
                        }
                    }

                }
            }
            catch (Exception ex) {
            writeFile.println("None");
            //writeFile.flush();
            return Boolean.FALSE;
        }
        }


    private static int ChecknodeisPresentInQueue(Deque<Node> queue,Node node)
    {
        int Flag=0;
        for (Node queueNode : queue) {
            //if (queueNode.State.equals(node.State) && node.PathCost==queueNode.PathCost) {
            if (queueNode.State.equals(node.State)) {
                Flag = 1;
                break;
            }
        }
        return Flag;
    }

    private static int CalculateParentPathCost(Node nodesList,Deque<Node> explored)
    {
        int flag=0;

            String Parent= nodesList.Parent;
            Integer ParentPathCost=0;
            if(explored.getLast().State.equals(Parent))
                {
                    ParentPathCost=explored.getLast().PathCost;
                    flag=1;
                }
                if(flag==1) {
                    if(ParentPathCost!=null) {
                        return ParentPathCost;
                    }
                }
        return ParentPathCost;
    }

    private static List<String> SortChildAplhabetically(List<Node> nodesList)
    {
        List<String> nodeNamesList= nodesList.stream().map(node -> node.State).collect(Collectors.toList());

        java.util.Collections.sort(nodeNamesList, Collator.getInstance());
        return nodeNamesList;
    }
    private static List<String> SortChildReverseAplhabetically(List<Node> nodesList)
    {
        List<String> nodeNamesList= nodesList.stream().map(node -> node.State).collect(Collectors.toList());

        java.util.Collections.sort(nodeNamesList, Collections.reverseOrder());
        return nodeNamesList;
    }
}


class Node
{
    String State="";
    String Parent="";
    Integer PathCost=0;
    Integer ParentPathCost=0;
    Integer Depth=0;
    Boolean Visited=false;
    Boolean offTimes=false;
    HashMap Time=new HashMap<Integer,Integer>();
}

class Edge
{
    String SourceNode="";
    String Algo="";
    HashMap<String, Integer> DestinationNodes=new HashMap<>();
    Integer NumOfPipes=0;
    HashMap<String, List<Node>> AdjList=new HashMap<String,List<Node>>();
    HashMap<String, String> existingNodes= new HashMap<String,String>();
    Integer StartTime=0;
}

class PriorityCompare implements Comparator<Node> {
@Override
public int compare(Node node1, Node node2)
{
    if(node1.PathCost==node2.PathCost)
    {
        return node1.State.compareTo(node2.State);

    }
    else
    {
        return node1.PathCost-node2.PathCost;
    }
}
}