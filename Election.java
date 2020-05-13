package col106.assignment3.Election;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import col106.assignment3.Heap.Heap;


public class Election implements ElectionInterface {
	/* 
	 * Do not touch the code inside the upcoming block 
	 * If anything tempered your marks will be directly cut to zero
	*/
	public static void main() {
		ElectionDriverCode EDC = new ElectionDriverCode();
		System.setOut(EDC.fileout());
	}
	/*
	 * end code
	 */


	class candidate{
		String name;
		String state;
		String district;
		String constituency;
		String party;

		public candidate(String na, String sta, String dist, String consti, String par){
			name = na;
			state = sta;
			district = dist;
			constituency = consti;
			party = par;
		}
	}


	private class node{
		node left;
		node right;
		String key;
		String value;
		candidate cand;

		public node(String name, String candID, String state, String district, String constituency, String party, String votes){
			key=candID;
			value= votes;
			left=null;
			right=null;
			cand = new candidate(name,  state,  district,  constituency,  party);
		}
	}


	public node root;

	HashMap<String, node> elect = new HashMap<String, node>();

	
	//write your code here 
    public void insert(String name, String candID, String state, String district, String constituency, String party, String votes){
    	node ptr = root;
    	if(root == null)
    	{
    		root = new node(name, candID, state, district, constituency, party, votes);
    		elect.put(candID, root);
    		return;
    	}
    	node temp = ptr;
    	int counter = 0;
    	while (ptr!=null){
    		if (Integer.parseInt(votes)> Integer.parseInt(ptr.value)){
    			temp = ptr;
    			ptr=ptr.right;
    			counter = 1;
    		}
    		else{
    			temp = ptr;
    			ptr = ptr.left;
    			counter = -1;
    		}
    	}
    	if(counter == 1)
    	{
    		temp.right = new node(name, candID, state, district, constituency, party, votes);
    		elect.put(candID,temp.right);

    	}
    	else
    	{
    		temp.left = new node(name, candID, state, district, constituency, party, votes);
    		elect.put(candID,temp.left);
    	}
    	return;
	}


	public void updateVote(String name, String candID, String votes){
		// printElectionLevelOrder();
		// System.out.println("0fff");
		node r = elect.get(candID);
		node upd= new node(name, candID, r.cand.state, r.cand.district, r.cand.constituency, r.cand.party, votes);
		delete(candID);
		elect.remove(candID);
		insert(name, candID, upd.cand.state, upd.cand.district, upd.cand.constituency, upd.cand.party, votes);
		// System.out.println(upd.key);
		// printElectionLevelOrder();
		// System.out.println("0fff");
	}


	public void delete(String key){

    	node ptr = elect.get(key);
    	root = deletei(ptr.value,root);
    
    }

    private node deletei(String value, node root){
    	if(root==null){
    		return root;
    	}
    	if(Integer.parseInt(value) < Integer.parseInt(root.value)){
    		root.left=deletei(value ,root.left);
    	}
    	else if(Integer.parseInt(value) > Integer.parseInt(root.value)){
    		root.right= deletei(value, root.right);
    	}

    	else{
    		if(root.left==null){
    			return root.right;
    		}
    		else if(root.right ==null){
    			return root.left;
    		}
    		root.value=(min_node(root.right)).value;
    		root.key=(min_node(root.right)).key;
    		root.cand=(min_node(root.right)).cand;
    		root.right = deletei(root.value, root.right);
    	}
    	return root;
    }




// report name, candID, party
	public void topkInConstituency(String constituency, String k){
		Heap<String, Integer> const_wise = new Heap<String, Integer>();  
		Iterator<String> itr = elect.keySet().iterator();
		int sizeh=0;
		while(itr.hasNext()){
			String x=itr.next();
			if(elect.get(x).cand.constituency.equals(constituency)){
				node n = elect.get(x);
				Integer i1=Integer.parseInt(n.value);
				const_wise.insert(n.key, i1);
				sizeh= sizeh+1;
			}
		}
		int k_= Integer.parseInt(k);
		int val;

		int min = k_;
		if(k_>sizeh) 
			min = sizeh;
		//if(k_<= sizeh){
			for(int i=0; i<min; i++){
				Iterator<String> itr1 = elect.keySet().iterator();
				val = const_wise.extractMax();
				while(itr1.hasNext()){
					String y=itr1.next();
					node n=elect.get(y);
					if(n.value.equals(Integer.toString(val)) && n.cand.constituency.equals(constituency)){
						System.out.println(n.cand.name + ", " + n.key + ", " + n.cand.party);
					}
				}
				//sizeh--;
			}
		//}
		// else{
		// 	for(int i=0; i<sizeh; i++){
		// 		val =const_wise.extractMax();
		// 		Iterator<String> itr1 = elect.keySet().iterator();
		// 		while(itr1.hasNext()){
		// 			String y=itr1.next();
		// 			node n=elect.get(y);
		// 			if(n.value.equals(val)){
		// 				System.out.println(n.cand.name + ", " + n.key + ", " + n.cand.party);
		// 			}
		// 		}
		// 		sizeh--;
		// 	}
		// }
		return;
	}


	class toppar{
		String party;
		String vote;

		toppar(String party, String vote){
			this.party = party;
			this.vote = vote;

		}
	}

// print party name
	public void leadingPartyInState(String state){
		Iterator<String> itr = elect.keySet().iterator();
		HashMap<String, toppar> leadp = new HashMap<String, toppar>();

		while(itr.hasNext()){
			String x= itr.next();
			node n = elect.get(x);
			if(elect.get(x).cand.state.equals(state)){
				toppar t = new toppar(n.cand.party, n.value);
				leadp.put(n.key, t);
			}
		}

		ArrayList<String> par = new ArrayList<String>(0);
		ArrayList<String> vot = new ArrayList<String>(0);

		Iterator<String> itr1 = leadp.keySet().iterator();

		while(itr1.hasNext()){
			String y = itr1.next();
			toppar t1 = leadp.get(y);
			if(inArray(par, t1.party)){
				int ind = par.indexOf(t1.party);
				int val= Integer.parseInt(t1.vote) + Integer.parseInt(vot.get(ind));
				vot.set(ind,Integer.toString(val));

			}
			else{
				par.add(t1.party);
				vot.add(t1.vote);
			}
		}
		// System.out.println(vot.size());
		// System.out.println(par.size());
		int max=Integer.parseInt(vot.get(0));
		for(int i=1; i<vot.size(); i++){
			if(Integer.parseInt(vot.get(i))> max){
				max= Integer.parseInt(vot.get(i));
			}
		}

		ArrayList<String> lex = new ArrayList<String>(0);
		for(int k =0; k<vot.size();k++){
			if(Integer.parseInt(vot.get(k)) == max){
				lex.add(par.get(k));
			}
		}
		lex = sort(lex);
		for(int k=0; k<lex.size(); k++){
			System.out.println(lex.get(k));
		}
	}

	public boolean inArray(ArrayList<String> a, String p){
		for(int i=0;i<a.size(); i++){
			if(a.get(i).equals(p)){
				return true;
			}
			
		}
		return false;
	}


/////////////////////////////////////////////
	////////////////////////////////////////
	/////////////////////////////////////

	// Traverse, starting from r
    private node min_node(node r){
    	if(r==null){
    		return null;
    	}

    	node min = r;
    	
    	while(r.left!=null){
    		min = r.left;
    		r = r.left;
    	}
    	return min;
    }

// Finding node for a particular key
    

    private node deleter(String value, node ptr){
    	if(ptr==null){
    		return ptr;
    	}
    	if(Integer.parseInt(value) < Integer.parseInt(ptr.value)){
    		ptr.left=deleter(value ,ptr.left);
    	}
    	else if(Integer.parseInt(value) > Integer.parseInt(ptr.value)){
    		ptr.right= deleter(value, ptr.right);
    	}

    	else{
    		if(ptr.left==null){
    			return ptr.right;
    		}
    		else if(ptr.right ==null){
    			return ptr.left;
    		}
    		ptr.key=min_node(ptr.right).key;
    		ptr.cand=min_node(ptr.right).cand;
    		ptr.value=min_node(ptr.right).value;
    		ptr.right = deleter(ptr.value, ptr.right);
    	}
    	return ptr;
    }


//  Same as the delete func (delete key)
	public void cancelVoteConstituency(String constituency){
		// printElectionLevelOrder();
		// System.out.println("fff");
		ArrayList<String> arr = new ArrayList<String>();
		Iterator<String> itr = elect.keySet().iterator();
		while(itr.hasNext()){
			String x= itr.next();
			node n= elect.get(x);
			if(constituency.equals(n.cand.constituency)){
				arr.add(n.key);
				//itr.remove();
			}
		}
		arr = sort(arr);
		//System.out.println(elect.size());
		// Iterator<String> itr1 = elect.keySet().iterator();
		// while(itr.hasNext()) {
		// 	String x1 = itr1.next();
		// 	node n1 = elect.get(x1);
		// 	f(constituency.equals(n.cand.constituency)){
		// 		arr.add(n.key);
		// 		//itr.remove();
		// 	}
		// }
		while(arr.size()!=0)
		{	

			String m = arr.remove(0);
			node n = elect.get(m);
			String vt = n.value;
			root = deleter(vt, root);
			elect.remove(m);
			
		}
		//System.out.println(elect.size());
		// printElectionLevelOrder();
	}


// print party name
	public void leadingPartyOverall(){
		Iterator<String> itr = elect.keySet().iterator();
		HashMap<String, toppar> Oleadp = new HashMap<String, toppar>();

		while(itr.hasNext()){
			String x= itr.next();
			node n = elect.get(x);
			toppar t = new toppar(n.cand.party, n.value);
			Oleadp.put(n.key, t);
		}

		ArrayList<String> par = new ArrayList<String>();
		ArrayList<String> vot = new ArrayList<String>();

		Iterator<String> itr1 = Oleadp.keySet().iterator();

		while(itr1.hasNext()){
			String y = itr1.next();
			toppar t1 = Oleadp.get(y);
			if(inArray(par, t1.party)){
				int ind = par.indexOf(t1.party);
				int val= Integer.parseInt(t1.vote) + Integer.parseInt(vot.get(ind));
				vot.set(ind,Integer.toString(val));

			}
			else{
				par.add(t1.party);
				vot.add(t1.vote);
			}
		}
		int max=Integer.parseInt(vot.get(0));
		for(int i=1; i<vot.size(); i++){
			if(Integer.parseInt(vot.get(i))> max){
				max= Integer.parseInt(vot.get(i));
			}
		}
		//int hsize=0;

		// System.out.println(par.get(0));
		ArrayList<String> lex = new ArrayList<String>(0);
		// Heap<String, String> tp = new Heap<String,String>();
		for(int k =0; k<vot.size();k++){
			if(Integer.parseInt(vot.get(k)) == max){
				lex.add(par.get(k));
			}
		}
		lex = sort(lex);
		for(int k=0; k<lex.size(); k++){
			System.out.println(lex.get(k));
		}
	}


	public ArrayList<String> sort(ArrayList<String> y){ 
		String min = y.get(0);
		for(int i =0;i<y.size();i++){

			for(int j =i;j<y.size();j++){

				min = y.get(i);
				if(min.compareTo(y.get(j))>0)
				{
					String temp = y.get(j);
					y.set(j,min);
					y.set(i,temp);
					min = temp;
				}
			}
		}
		return y;
	}



	public void voteShareInState(String party,String state){
		Iterator<String> itr = elect.keySet().iterator();
		HashMap<String, toppar> leadp = new HashMap<String, toppar>();

		while(itr.hasNext()){
			String x= itr.next();
			node n = elect.get(x);
			if(elect.get(x).cand.state.equals(state)){
				toppar t = new toppar(n.cand.party, n.value);
				leadp.put(n.key, t);
			}
		}

		ArrayList<String> par = new ArrayList<String>(0);
		ArrayList<String> vot = new ArrayList<String>(0);

		Iterator<String> itr1 = leadp.keySet().iterator();

		while(itr1.hasNext()){
			String y = itr1.next();
			toppar t1 = leadp.get(y);
			if(inArray(par, t1.party)){
				int ind = par.indexOf(t1.party);
				int val= Integer.parseInt(t1.vote) + Integer.parseInt(vot.get(ind));
				vot.set(ind,Integer.toString(val));

			}
			else{
				par.add(t1.party);
				vot.add(t1.vote);
			}
		}

		// System.out.println(par.get(0));
		
		int sum = Integer.parseInt(vot.get(0));

		for(int i=1; i<vot.size(); i++){
			sum= sum + Integer.parseInt(vot.get(i));
		}

		int partyv = Integer.parseInt(vot.get(par.indexOf(party)));
		// System.out.println(partyv);
		// System.out.println(sum);
		System.out.println((100*partyv)/sum);

	}
	
	public void printElectionLevelOrder() {
		if(root==null){
    		return;
    	}
    	else{
    		node ptr;
    		node print;
    		Queue<node> q = new LinkedList<>();
    		q.add(root);
    		while(q.size()!=0){
    			ptr = q.element();
    			if(ptr.left!= null){
    				q.add(ptr.left);
    			}
    			if(ptr.right != null){
    				q.add(ptr.right);
    			}
    			print=q.remove();
    			System.out.println(print.cand.name + ", " + print.key + ", " +print.cand.state + ", " + print.cand.district + ", " + print.cand.constituency + ", " + print.cand.party + ", " + print.value);
    		}
    	}		
	}
}