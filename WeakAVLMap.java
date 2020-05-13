package col106.assignment4.WeakAVLMap;
import java.util.Vector;
import java.util.Queue;
import java.util.LinkedList;

public class WeakAVLMap<K extends Comparable,V> implements WeakAVLMapInterface<K,V>{

	
	private class node<K extends Comparable,V>{
		node<K, V> left;
		node<K, V> right;
		node<K, V> parent;
		K ki;
		V val;
		int rank;

		public node(K key, V value){
			ki =key;
			val= value;
			left= null;
			right =null;
			rank=1;
			parent =null;
		}
	}

	node<K, V> root;
	int rotateC;

	public WeakAVLMap(){
		root =null;
		rotateC = 0;
	}
	
	public V put(K key, V value){
		node<K,V> ptr= root;
		node<K,V> ptr_p = null;
		if(root==null){
			root = new node(key, value);
			return null;
		}
		else{
			int counter =0;
			while(ptr!=null){
				if(key.compareTo(ptr.ki)<0){
					ptr_p =ptr;
					ptr= ptr.left;
					counter =1;
				}

				else if(key.compareTo(ptr.ki) > 0){
					ptr_p =ptr;
					ptr= ptr.right;
					counter =-1;
				}

				else{
					V out = ptr.val;
					ptr.val = value;
					return out;	
				}
			}
			if(counter ==1){
				ptr_p.left = new node(key, value);
				ptr = ptr_p.left;
			}
			else{
				ptr_p.right = new node(key, value);
				ptr = ptr_p.right;
			}
			ptr.parent = ptr_p;
		}
		// now checking/ fixing the tree
		
/////////////////////////////////////////////////////////
// more cases needs to be added i guess, for other parent nodes ie ptr rank =2

		while(ptr != null){
			if(ptr == root || rank_diff(ptr_p, ptr) == 1){
			return null;
			}
			ptr_p = ptr.parent;
			if(rank_diff(ptr_p, ptr) == 0){
				node<K,V> sibl = siblin(ptr_p, ptr);
				if(rank_diff(ptr_p, sibl) == 1){
					ptr.parent.rank ++;
					ptr = ptr.parent;
				}
				else if(rank_diff(ptr_p, sibl) == 2){
					node<K,V> t =  get1diff_child(ptr);
					node<K,V> p = ptr.parent;
					String str= travel_path(p, ptr, t);

					if(str.charAt(0)== (str.charAt(1))){
						if(p==root){
							root= ptr;
						}
						else{
							if(p.parent.right== p){
								p.parent.right=ptr;
							}
							else{
								p.parent.left=ptr;
							}
						}

						if(str.charAt(0)=='L'){
							ptr.parent = p.parent;
							p.rank = ptr.rank -1;
							p.left= ptr.right;
							if(ptr.right != null){
								ptr.right.parent = p;
							}
							ptr.right =p;
							p.parent = ptr;
							rotateC++;
							// return null;
						}
						else{
							ptr.parent = p.parent;
							p.rank = ptr.rank -1;
							p.right= ptr.left;
							if(ptr.left!= null){
								ptr.left.parent = p;
							}
							ptr.left =p;
							p.parent = ptr;
							rotateC++;
							// return null;
						}
						return null;
					}
					else{
						if(p==root){
							root= t;
						}
						else{
							if(p.parent.right== p){
								p.parent.right=t;
							}
							else{
								p.parent.left=t;
							}
						}
						if(str.charAt(0)=='L'){
							t.rank = p.rank;
							t.parent=p.parent;
							p.rank--;
							ptr.rank--;
							ptr.right = t.left;
							if(t.left !=null){
								t.left.parent= ptr;
							}
							p.left = t.right;
							if(t.right!=null){
								t.right.parent = p;
							}
							t.left= ptr;
							ptr.parent = t;
							t.right= p;
							p.parent = t;
							rotateC +=2;
						}
						else{
							t.rank = p.rank;
							t.parent=p.parent;
							p.rank--;
							ptr.rank--;
							ptr.left = t.right;
							if(t.right != null){
								t.right.parent= ptr;
							}
							p.right = t.left;
							if(t.left !=null){
								t.left.parent = p;
							}
							t.right= ptr;
							ptr.parent = t;
							t.left= p;
							p.parent = t;
							rotateC +=2;
						}
					return null;
					}
				}
			}
		}
		return null;
	}


	public String travel_path(node<K,V> p, node<K,V> q, node<K,V> t){
		String s ="";
		if(p.left == q){
			s =s + "L";
		}
		else{
			s= s+"R";
		}

		if(q.left == t){
			s =s + "L";
		}
		else{
			s= s+"R";
		}

		return s;
	}


	private node<K,V> get1diff_child( node<K,V> n1){
		if(rank_diff(n1, n1.left) == 1){
			return n1.left;
		}
		else{
			return n1.right;
		}
	}


	public V remove(K key){
		node<K,V> ptr= root;
		if(root==null){
    		return null;
    	}
    	while(ptr!=null){
	    	if(key.compareTo(ptr.ki)<0){
	    		ptr = ptr.left;
	    	}
	    	else if(key.compareTo(ptr.ki)>0){
	    		ptr = ptr.right;
	    	}

	    	else{
	    		if(ptr.left==null){
	    			return restructure(ptr, ptr.val);
	    		}
	    		else if(ptr.right ==null){
	    			return restructure(ptr, ptr.val);
	    		}
	    		// for internal node
	    		else{
	    			V out = ptr.val;
		    		ptr.ki = min_node(ptr.right).ki;
		    		ptr.val = min_node(ptr.right).val;
		    		return restructure(min_node(ptr.right), out);
		    		// here comes a function for restructuring to which only external node is passed
	    		}	
	    	}
    	}
    	return null;
	}


// restructure function has to return the next node.
// here v is the node to be deleted and is an external node
    private V restructure(node<K,V> v, V value){
    	node<K,V> u;
    	node<K,V> q;
    	node<K,V> p;
    	
		if(v.left == null){
			u = v.left;
			q = v.right;
		}
		else{
			q = v.left;
			u = v.right;
		}

		if(v== root){
			if(q!= null){
				q.parent =null;
			}
			root =q;
			return value;
		}
		else{
			p = v.parent;
			if(p.left == v){
				p.left = q;
				if(q!= null){
					q.parent =p;
				}
			}
			else{
				p.right = q;
				if(q!= null){
					q.parent =p;
				}
			}
		}

		while(q!= root){
			node<K,V> s= siblin(p,q);
			if(rank_diff(p,q) == 2){
				if(rank_diff(p,s) ==1){
					return value;
				}
				else if(rank_diff(p,s) ==2){
					if(s== null && q== null){
						p.rank--;
						q=p;
					}
					else {
						return value;
					}		
				}
			}
			
			else if(rank_diff(p, q) == 3){
				
				if(rank_diff(p, s) == 2){
					p.rank--;
					q=p;
				}
				else if(rank_diff(p,s)==1){
					if(rank_diff(s, s.left) == 2 && rank_diff(s, s.right)==2){
						p.rank--;
						s.rank--;
						q=p;
					}

					else if(rank_diff(s, s.left) ==1 ){
						node<K,V> t = s.left;
						String str= travel_path(p, s, s.left);
						// have to extensively use paths
						// s is a left child LL case

						if(str.charAt(0)=='L'){
							if(p==root){
								root= s;
							}
							else{
								if(p.parent.right== p){
									p.parent.right=s;
								}
								else{
									p.parent.left=s;
								}
							}

							s.parent= p.parent;
							s.rank++;
							p.parent =s;
							p.rank--;
							p.left = s.right;
							if(s.right != null){
								s.right.parent = p;
							}
							s.right =p;
							rotateC++;
						}
						else{
					
							s.rank--;
							p.rank = p.rank-2;
							t.rank =t.rank+2;
							s.left= t.right;

							if(t.right!=null){
								t.right.parent= s;
							}
							t.parent = p.parent;
							if(p==root){
								root= t;
							}
							else{
								if(p.parent.right== p){
									p.parent.right=t;
								}
								else{
									p.parent.left=t;
								}
							}

							p.right = t.left;

							if(t.left!= null){
								t.left.parent = p;
							}
							p.parent = t;
							s.parent = t;
							t.right =s;
							t.left=p;
							rotateC+=2;
						}
						return value;
					}
					else if( rank_diff(s, s.right)==1){
						String str= travel_path(p, s, s.right);
						node<K,V> t = s.right;
						if(str.charAt(0)=='R'){
							if(p==root){
								root= s;
							}
							else{
								if(p.parent.right== p){
									p.parent.right=s;
								}
								else{
									p.parent.left=s;
								}
							}

							s.parent= p.parent;
							s.rank++;
							p.parent =s;
							p.rank--;
							p.right = s.left;
							if(s.left != null){
								s.left.parent = p;
							}
							s.left =p;
							rotateC++;
						}
						else{
							s.rank--;
							p.rank = p.rank-2;
							t.rank =t.rank+2;
							s.right= t.left;
							if(t.left!= null){
								t.left.parent = s;
							}
							t.parent = p.parent;
							if(p==root){
								root= t;
							}
							else{
								if(p.parent.right== p){
									p.parent.right=t;
								}
								else{
									p.parent.left=t;
								}
							}
							p.left = t.right;
							if(t.right!=null){
								t.right.parent= p;
							}
							p.parent = t;
							s.parent = t;
							t.left =s;
							t.right=p;
							rotateC+=2;
						}
						return value;
					}
				}
			}
			p=p.parent;
		}
		return value;
	}


						
	public node<K,V> siblin(node<K,V> par, node<K,V> child){
		if(child == null){
			if(par.left == null){
				return par.right;
			}
			else{
				return par.left;
			}
		}
		else{
			if(par.left == child){
				return par.right;
			}
			else{
				return par.left;
			}
		}
	} 
		
// this gives rank diff of child, not defined for root
    private int rank_diff( node<K,V> par, node<K,V> child){
    	if (child == null){
    		return par.rank;
    	}
    	else{
    		return (par.rank - child.rank);
    	}
    }


    private node<K,V> min_node(node<K,V> r){
    	if(r==null){
    		return null;
    	}

    	node<K,V> min = r;
    	
    	while(r.left!=null){
    		min = r.left;
    		r = r.left;
    	}
    	return min;

    }




// search function
	public V get(K key){
		node<K,V> ptr = root;
		while(ptr!=null){
			if(key.compareTo(ptr.ki)<0){
				ptr= ptr.left;
			}
			else if(key.compareTo(ptr.ki)>0){
				ptr= ptr.right;
			}
			else{
				return ptr.val;
			}
		}
		return null;
	}



	public Vector<V> searchRange(K key1, K key2){
		Vector<V> out = new Vector<V>();
		ans = new Vector<V>();
		if(root!= null){
			out = inorder(key1, key2, root);
		}
		return out;
	}

// creating a class vector to perform recursion
	Vector<V> ans;
	public Vector<V> inorder(K key1, K key2, node<K,V> n){

		if(n.left != null){
			inorder(key1, key2, n.left);
		}
		if(n.ki.compareTo(key1) >=0 && n.ki.compareTo(key2) <=0){
			ans.add(n.val);
		}
		
		if(n.right != null){
			inorder(key1, key2, n.right);
		}
		return ans;
	}

	public int rotateCount(){		
		return rotateC;
	}



	public int getHeight(){
		if(root ==null){
			return 0;
		} 
		else{
			return maxd(root);
		}
	}


	public int maxd(node<K,V> n){
		if(n== null){
			return 0;
		}
		else{
			int r_d = maxd(n.right);
			int l_d = maxd(n.left);

			if( r_d > l_d){
				return (r_d + 1);
			}
			else{
				return (l_d + 1);
			}
		}
	}

	public Vector<K> BFS(){
		Vector<K> a = new Vector<K>();
		Queue<node> q = new LinkedList<>();
		if(root ==null){
			return a;
		}

		q.add(root);
		while(q.size()!=0){
			node<K,V> ptr = q.peek();
			if(ptr.left != null){
				q.add(ptr.left);
			}
			if(ptr.right != null){
				q.add(ptr.right);
			}
			node<K,V> answer = q.remove();
			a.add(answer.ki);	
		}
		return a;
	}
}
