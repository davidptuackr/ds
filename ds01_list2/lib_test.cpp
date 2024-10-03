#include <iostream>
#include <vector>
#include <list>
#include <deque>
#include <forward_list>
#include <queue>

int main() {

    // Create a vector of integers
    std::vector<int> vec;

    // Add elements to the vector
    vec.push_back(1);
    vec.push_back(2);
    vec.push_back(3);

    // Access and modify elements
    vec[0] = 10;

    // Print the elements
    std::cout << "VECTOR" << " : ";
    for (int i = 0; i < vec.size(); ++i) {
        std::cout << vec[i] << " ";
    }
    std::cout << std::endl << std::endl;


    std::list<int> lst;

    // Add elements to the list
    lst.push_back(1);
    lst.push_back(2);
    lst.push_back(3);

    // Insert element at the beginning
    lst.push_front(0);

    std::list<int>::iterator it = lst.begin();
    std::advance(it, 2);
    lst.insert(it, 999);

    // Print the elements
    std::cout << "LIST" << " : ";
    for (auto it = lst.begin(); it != lst.end(); ++it) {
        std::cout << *it << " ";
    }
    std::cout << std::endl << std::endl;

    std::deque<int> deq;

    // Add elements to the deque
    deq.push_back(1);
    deq.push_back(2);
    deq.push_back(3);

    // Insert element at the beginning
    deq.push_front(0);

    // Print the elements
    std::cout << "DEQUE" << " : ";
    for (int i = 0; i < deq.size(); ++i) {
        std::cout << deq[i] << " ";
    }
    std::cout << std::endl << std::endl;
    
    std::forward_list<int> flst;

    // Add elements to the forward list
    flst.push_front(1);
    flst.push_front(2);
    flst.push_front(3);

    // Insert element at the beginning
    flst.push_front(0);

    // Print the elements
    for (auto it = flst.begin(); it != flst.end(); ++it) {
        std::cout << *it << " ";
    }
    std::cout << std::endl;

    std::cout << vec.capacity() << std::endl;
    std::cout << vec.size() << std::endl;
    
    /*std::list<int>::const_iterator it2 = lst.cbegin();
    lst.splice(it2, 1);*/

    

    return 0;
}

void pnr_test(int* p, int& r)
{

}