EnchantmentListeners are the way you listen to events.

EnchantmentListeners are easy to make, just create a new method, and annotate it with:

@EnchantmentListener(type = ListenerType.TYPE)

With TYPE as the listener type you are listening for.

The method has to return a EnchantmentListener<EnchantmentEvent<Event>>.

Luckily, EnchantmentListener is a FunctionalInterface, so you can use something like the following method:

```
@EnchantmentListener(type = ListenerType.BLOCKBREAK)
public EnchantmentListener<EnchantmentEvent<BlockBreakEvent>> onBlockBreak() {
    return (event) -> {
        event.getUser().sendMessage("Block break!");
    };
}
```

See [Events.md](development/Events.md) for a list of events.