package rest.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import rest.entity.Fruit;

@Path("fruits")
public class FruitResource {

	private List<Fruit> fruits = new ArrayList<>();

	public FruitResource() {
		Fruit f = new Fruit();
		f.setId(1);
		f.setName("pomme");
		fruits.add(f);
		f = new Fruit();
		f.setId(2);
		f.setName("clementine");
		fruits.add(f);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFruits() {
		return Response.ok(fruits).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFruit(@PathParam("id") int id) {
		return Response.ok(fruits.stream().filter(
				fruit -> id == fruit.getId())
				.findAny().orElse(null)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFruit(Fruit fruit) {
		fruits.add(fruit);
		return Response.ok(fruits).build();
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Fruit newFruit) {
		Fruit oldFruit = fruits.stream().filter(f -> id == f.getId()).findAny().orElse(null);
		if (oldFruit == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		fruits.remove(id);
		fruits.add(newFruit);
		return Response.ok(fruits).build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		fruits.remove(id);
		return Response.ok(fruits).build();
	}

	@GET
	@Path("_search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("id") Integer id, @QueryParam("name") String name) {
		if (id != null && name != null) {
			return Response.ok(fruits.stream().filter(fruit -> id == fruit.getId() && name.contains(fruit.getName()))
					.findAny().orElse(null)).build();
		} else if (id != null && name == null) {
			return Response.ok(fruits.stream().filter(fruit -> id == fruit.getId()).findAny().orElse(null)).build();
		} else if (id == null && name != null) {
			return Response.ok(fruits.stream().filter(fruit -> name.contains(fruit.getName())).findAny().orElse(null))
					.build();
		} else {
			return Response.ok(fruits).build();
		}
	}

}

