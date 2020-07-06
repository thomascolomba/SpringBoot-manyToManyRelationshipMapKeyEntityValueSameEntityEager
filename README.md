CRUD operations on a ManyToMany relationship with a java Map<Entity1, Entity2> type with Eager fetch.<br/>
An instance of A has a Map<B, B> (B is an entity that's both the key and value of the Map).<br/>
<br/>
compile & execute :<br/>
mvn spring-boot:run<br/>
compile into fat jar then execute :<br/>
mvn clean package<br/>
java -jar target/manyToManyRelationshipMapKeyEntityValueSameEntityEager-0.0.1-SNAPSHOT.jar<br/>
<br/>
To Compile from within Eclipse or any other IDE, you need to install Lombok : https://projectlombok.org/setup/overview<br/>
<br/>
<br/>
persisting/modifying/deleting an A will persist/modify/delete the corresponding relation with Bs in the join table.
--A.java<br/>
@ManyToMany(fetch = FetchType.EAGER)<br/>
private Map&lt;B, B&gt; myMap;<br/>

--AccessingDataJpaApplication.java (main class)<br/>
log.info("===== Persisting As and Bs");<br/>
persistData(aRepository, bRepository, cRepository);<br/>
readData(aRepository, bRepository, cRepository);<br/>
log.info("===== Modifying some As and Bs");<br/>
modifyData(aRepository, bRepository, cRepository);<br/>
readData(aRepository, bRepository, cRepository);<br/>
log.info("===== Deleting some As and Bs");<br/>
deleteData(aRepository, bRepository, cRepository);<br/>
readData(aRepository, bRepository, cRepository);<br/>
...<br/>
<b>persistData(){</b><br/>
&nbsp;&nbsp;//we build : a1.myMap{ b1 -> b1} and a2.myMap{ b1 -> b1, b2 -> b3, b3 -> b3, b4 ->b5}<br/>
&nbsp;&nbsp;A a1 = new A("a1");<br/>
&nbsp;&nbsp;A a2 = new A("a2");<br/>
&nbsp;&nbsp;B b1 = new B("b1");<br/>
&nbsp;&nbsp;B b2 = new B("b2");<br/>
&nbsp;&nbsp;B b3 = new B("b3");<br/>
&nbsp;&nbsp;B b4 = new B("b4");<br/>
&nbsp;&nbsp;B b5 = new B("b5");<br/>
&nbsp;&nbsp;Map<B,B> a1Map = new HashMap<B, B>();<br/>
&nbsp;&nbsp;a1Map.put(b1,b1);<br/>
&nbsp;&nbsp;Map<B,B> a2Map = new HashMap<B, B>();<br/>
&nbsp;&nbsp;a2Map.put(b1,b1);<br/>
&nbsp;&nbsp;a2Map.put(b2,b3);<br/>
&nbsp;&nbsp;a2Map.put(b3,b3);<br/>
&nbsp;&nbsp;a2Map.put(b4,b5);<br/>
&nbsp;&nbsp;a1.setMyMap(a1Map);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;a2.setMyMap(a2Map);<br/>
&nbsp;&nbsp;bRepository.save(b1);<br/>
&nbsp;&nbsp;bRepository.save(b2);<br/>
&nbsp;&nbsp;bRepository.save(b3);<br/>
&nbsp;&nbsp;bRepository.save(b4);<br/>
&nbsp;&nbsp;bRepository.save(b5);<br/>
&nbsp;&nbsp;aRepository.save(a1);<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
}<br/>
<b>modifyData(){</b><br/>
&nbsp;&nbsp;//we move the b2 entry from a2 to a1 and switch values between b3 and b4 entries in a2<br/>
&nbsp;&nbsp;A a1 = aRepository.findByA("a1").get(0);<br/>
&nbsp;&nbsp;A a2 = aRepository.findByA("a2").get(0);<br/>
&nbsp;&nbsp;B b2 = bRepository.findByB("b2").get(0);<br/>
&nbsp;&nbsp;B b3 = bRepository.findByB("b3").get(0);<br/>
&nbsp;&nbsp;B b4 = bRepository.findByB("b4").get(0);<br/>
&nbsp;&nbsp;a1.getMyMap().put(b2, a2.getMyMap().get(b2));<br/>
&nbsp;&nbsp;B a2_b3 = a2.getMyMap().get(b3);<br/>
&nbsp;&nbsp;B a2_b4 = a2.getMyMap().get(b4);<br/>
&nbsp;&nbsp;a2.getMyMap().put(b3, a2_b4);<br/>
&nbsp;&nbsp;a2.getMyMap().put(b4, a2_b3);<br/>
&nbsp;&nbsp;aRepository.save(a1);<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
}<br/>
<b>deleteData(){</b><br/>
&nbsp;&nbsp;//we delete a1, a2.b1 and a2's entries of value b3<br/>
&nbsp;&nbsp;A a1 = aRepository.findByA("a1").get(0);<br/>
&nbsp;&nbsp;A a2 = aRepository.findByA("a2").get(0);<br/>
&nbsp;&nbsp;B b1 = bRepository.findByB("b1").get(0);<br/>
&nbsp;&nbsp;B b3 = bRepository.findByB("b3").get(0);<br/>
&nbsp;&nbsp;aRepository.delete(a1);<br/>
&nbsp;&nbsp;a2.getMyMap().remove(b1);<br/>
&nbsp;&nbsp;Iterator<Map.Entry<B, B>> iterator = a2.getMyMap().entrySet().iterator();<br/>
&nbsp;&nbsp;while (iterator.hasNext()) {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Map.Entry<B, B> entry = iterator.next();<br/>
&nbsp;&nbsp;&nbsp;&nbsp;if(entry.getValue().equals(b3)) {<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;iterator.remove();<br/>
&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
&nbsp;&nbsp;}<br/>
&nbsp;&nbsp;aRepository.save(a2);<br/>
}<br/>